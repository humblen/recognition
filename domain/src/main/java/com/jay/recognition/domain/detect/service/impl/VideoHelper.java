package com.jay.recognition.domain.detect.service.impl;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.*;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

import org.bytedeco.javacv.*;

import java.io.File;
import java.util.Date;
import java.io.File;

public class VideoHelper {

    private static final Logger logger = LoggerFactory.getLogger(VideoHelper.class);

    public static boolean AviToMp4(String oldPath, String newPath) {
//    File source = new File("D:\\temp\\1.avi");
//     File target = new File("D:\\file\\1.mp4");
        File source = new File(oldPath);
        File target = new File(newPath);
        logger.info("转换后的路径:" + newPath);
        AudioAttributes audio = new AudioAttributes();
        //音频编码格式
        audio.setCodec("libmp3lame");
        audio.setBitRate((800000));
        audio.setChannels((1));
        VideoAttributes video = new VideoAttributes();
        //视频编码格式
        video.setCodec("libx264");
        video.setBitRate((6000000));
        //数字设置小了，视频会卡顿
        video.setFrameRate((15));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        MultimediaObject multimediaObject = new MultimediaObject(source);
        try {
            logger.info("avi转MP4 --- 转换开始:" + new Date());
            encoder.encode(multimediaObject, target, attrs);
            logger.info("avi转MP4 --- 转换结束:" + new Date());
            return true;
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InputFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (EncoderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static void convert(String inputPath, String outputPath, String format) throws FrameRecorder.Exception {
        try {
            // 创建 FFmpegFrameGrabber 对象，用于读取输入视频文件
            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputPath);
            // 启动帧抓取器
            frameGrabber.start();

            // 创建 FFmpegFrameRecorder 对象，用于将抓取的帧写入输出文件
            FFmpegFrameRecorder frameRecorder = new FFmpegFrameRecorder(outputPath, frameGrabber.getImageWidth(), frameGrabber.getImageHeight(), frameGrabber.getAudioChannels());

// ========== 关键参数设置 ==========
            // 强制指定编码格式
            frameRecorder
                    .setVideoCodec(avcodec.AV_CODEC_ID_H264);  // H.264 编码
            frameRecorder
                    .setAudioCodec(avcodec.AV_CODEC_ID_AAC);   // AAC 音频编码
            frameRecorder
                    .setPixelFormat(avutil.AV_PIX_FMT_YUV420P); // 像素格式
            frameRecorder
                    .setFormat("mp4");                         // 容器格式
            frameRecorder
                    .setOption("movflags", "+faststart");      // 流式优化
            frameRecorder
                    .setGopSize(60);                           // 关键帧间隔（建议 2秒*帧率）
            frameRecorder
                    .setVideoQuality(0);                       // 0=最高质量（CRF模式）
            frameRecorder
                    .setVideoOption("preset", "slow");         // 编码速度与质量平衡
            frameRecorder
                    .setVideoBitrate(2_000_000);               // 比特率（按需调整）

            // 设置输出文件的格式
            frameRecorder.setFormat(format);
            // 设置输出视频的帧率
            frameRecorder.setFrameRate(frameGrabber.getFrameRate());
            // 设置输出音频的采样率
            frameRecorder.setSampleRate(frameGrabber.getSampleRate());
            // 启动帧记录器
            frameRecorder.start();

            Frame frame;
            // 循环抓取输入视频的每一帧，直到没有更多帧为止
            while ((frame = frameGrabber.grabFrame()) != null) {
                // 将抓取的帧写入输出文件
                frameRecorder.record(frame);
            }

            // 停止帧记录器
            frameRecorder.stop();
            // 停止帧抓取器
            frameGrabber.stop();
        } catch (FrameGrabber.Exception e) {
            // 若发生异常，记录错误日志
            logger.error("convert failed", e);
        }
    }
}
