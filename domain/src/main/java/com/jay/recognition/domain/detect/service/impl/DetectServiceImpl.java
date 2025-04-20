package com.jay.recognition.domain.detect.service.impl;

import com.jay.recognition.domain.detect.dao.Detection;
import com.jay.recognition.domain.detect.dto.ImageDTO;
import com.jay.recognition.domain.detect.dto.ImageParamDTO;
import com.jay.recognition.domain.detect.mapper.DetectMapper;
import com.jay.recognition.domain.detect.service.DetectService;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.jay.recognition.domain.detect.service.impl.DetectHelper.*;

@Service
@Log4j
public class DetectServiceImpl implements DetectService {
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    DetectHelper modelConfig;

    @Autowired
    DetectMapper detectMapper;
    private final Float TRACK_IOU = 0.8f;
    private final Float TRACK_CONF = 0.8f;

    private final String TRACK_MODEL = "track_yolon300epoch.pt";

    private final String PYTHON_INTERPRETER = "E:\\Anaconda\\envs\\yolov11\\python.exe";
    private final String PYTHON_SCRIPT = "E:\\pycharm\\python_projects\\demo\\yolo\\script.py";
    private static final Logger logger = LoggerFactory.getLogger(DetectServiceImpl.class);

    @Override
    public ResponseEntity<Resource> detect(ImageDTO imageDTO) {
        logger.info("--start detect--");
        //上传的原图片保存路径
        String savePath = saveImage(imageDTO.getImage());
        logger.info("--save successfully--");
        //检测结果保存的父目录路径，作为参数传给python
        String resultParent = "E:\\Idea\\Ultimate_projects\\recognition\\runs\\detect\\";
        ImageParamDTO imageParamDTO = new ImageParamDTO(savePath, imageDTO.getIOU(), imageDTO.getConfidence(), imageDTO.getModel(), resultParent);
        //结果图片的绝对路径
        String resultPath = detectImage(imageParamDTO);
        logger.info("--detect successfully--");
        Path parent = Paths.get(resultPath).getParent();
        detectTrack(parent);
        logger.info("--save info successfully--");
        ResponseEntity<Resource> resourceResponseEntity = buildResult(resultPath);
        return resourceResponseEntity;
    }

    private static ResponseEntity<Resource> buildResult(String resultPath) {
        logger.info("image Path:" + resultPath);
        File file = new File(resultPath);
        Resource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + file.getName());
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        logger.info("headers: " + headers);

        ResponseEntity<Resource> body = ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
        return body;
    }

    @Override
    public String saveImage(MultipartFile image) {
        try {
            // 读取上传的图片
            InputStream inputStream = image.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = outputStream.toByteArray();

            // 将图片保存到本地
            String savePath = "\\uploads\\" + UUID.randomUUID() + image.getOriginalFilename();
            String currentWorkingDir = System.getProperty("user.dir");
            savePath = currentWorkingDir + savePath;
            Path path = Paths.get(savePath);
            Files.createDirectories(path.getParent()); // 创建目录
            try (FileOutputStream fos = new FileOutputStream(savePath)) {
                fos.write(imageBytes);
            }
            return savePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String detectImage(ImageParamDTO imageParamDTO) {
        try {
            Process process = getProcess(imageParamDTO);
            // 获取进程的输入流并读取输出
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder stdOutput = new StringBuilder();
            String line;
            while ((line = stdInput.readLine()) != null) {
                stdOutput.append(line).append("\n");
            }
            // 处理标准错误流
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errorOutput = new StringBuilder();
            while ((line = stdError.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
            // 等待进程执行完毕
            int exitCode = process.waitFor();
            if (exitCode != 0) logger.error(errorOutput.toString());
            logger.info("Process exited with code " + exitCode);
            logger.info("python output: " + stdOutput);
            String[] split = imageParamDTO.getPath().split("\\\\");
            String fileName = split[split.length - 1];
            return imageParamDTO.getSavePath() + fileName + "\\" + fileName;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveDetectResult(Detection detection) {
        detectMapper.insertDetection(detection);
    }

    @Override
    public void detectTrack(Path path) {
        Path crops = path.resolve("crops");
        // 检查 crops 文件夹是否存在
        if (Files.exists(crops) && Files.isDirectory(crops)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(crops)) {
                for (Path label : stream) {
                    if (Files.isDirectory(label)) {
                        DirectoryStream<Path> files = Files.newDirectoryStream(label);
                        for (Path file : files) {
                            if (file.endsWith(".jpg")) {
                                ImageParamDTO imageParamDTO = new ImageParamDTO(file.toAbsolutePath().toString(),
                                        TRACK_IOU, TRACK_CONF,
                                        TRACK_MODEL,
                                        label.toAbsolutePath().toString());
                                String result = detectImage(imageParamDTO);
                                File labelDir = new File(new File(result).getParent() + "\\labels");
                                List<Integer> list = null;
                                File[] files1 = labelDir.listFiles();
                                if (files1 != null) {
                                    for (File listFile : files1) {
                                        list = parseFirstNumbers(listFile);
                                    }
                                }
                                Integer driver = (list == null || list.isEmpty()) ? -1 : list.getFirst();
                                Detection detection = new Detection(DRIVER.get(driver),
                                        status2Code(label.getFileName().toString()),
                                        "",
                                        LocalDateTime.now());
                                saveDetectResult(detection);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        } else logger.info("crops not exists: " + crops);
    }

    private Process getProcess(ImageParamDTO imageParamDTO) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String path = imageParamDTO.getPath();

        // 创建 ProcessBuilder 对象
//        ProcessBuilder processBuilder = new ProcessBuilder(
//                PYTHON_INTERPRETER,
//                PYTHON_SCRIPT,
//                path,
//                String.valueOf(imageParamDTO.getIOU()),
//                String.valueOf(imageParamDTO.getConfidence()),
//                modelConvert(imageParamDTO.getModel()));

        // 启动进程
        String command = PYTHON_INTERPRETER + " " + PYTHON_SCRIPT + " " + path +
                " " + imageParamDTO.getIOU() + " " + imageParamDTO.getConfidence() +
                " " + chooseModel(imageParamDTO.getModel() + " " + imageParamDTO.getSavePath());
        logger.info("command:" + command);
        Process process = runtime.exec(command);
        return process;
    }

    public List<Integer> parseFirstNumbers(File file) {
        List<Integer> numbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 按空格分割每行内容
                String[] parts = line.split(" ");
                if (parts.length > 0) {
                    try {
                        // 将第一个元素转换为整数
                        int firstNumber = Integer.parseInt(parts[0]);
                        numbers.add(firstNumber);
                    } catch (NumberFormatException e) {
                        System.err.println("无法将首元素转换为整数: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("读取文件时出错: " + e.getMessage());
        }
        return numbers;
    }
}
