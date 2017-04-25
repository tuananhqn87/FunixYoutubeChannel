package org.anhtran.funixyoutubechannel.video;

import java.util.ArrayList;

/**
 * Created by anhtran on 4/18/17.
 */

public class VideoList {
    private ArrayList<Video> mVideoList;

    private VideoList() {
    }

    public static ArrayList<Video> getBeginnersList() {

        ArrayList<Video> videos = new ArrayList<>();

        videos.add(new Video("JUcyQlt0H2U", "Hướng dẫn cài đặt và sử dụng FUNiX Passport"));
        videos.add(new Video("IkWc7k0IQYI", "Hướng dẫn học và làm bài kiểm tra trên Hệ thống học tập LMS"));
        videos.add(new Video("vfRTTojRykA", "Hướng dẫn đăng nhập và sử dụng xRoom"));
        videos.add(new Video("iSDOpmaobFY", "Mô phỏng phiên hỏi thi cuối môn vấn đáp online tại FUNiX"));
        videos.add(new Video("DUcybLn7PLo", "Làm chủ máy tính"));
        videos.add(new Video("58VuG_vcXqE", "Trở thành công dân số"));
        videos.add(new Video("YLQp3k3AHJM", "Tự xây dựng website"));
        videos.add(new Video("SYBmPZdBFIU", "Tự xây dựng phần mềm"));

        return videos;
    }

    public static ArrayList<Video> getHannahDiaryList() {
        ArrayList<Video> videos = new ArrayList<>();

        videos.add(new Video("iYKmxxIzEiQ", "[Nhật ký Day1] Hannah là ai?"));
        videos.add(new Video("kiv161mjxfs", "[NKHN Day25] 10 chiêu \"thả thính\" của Hannah"));
        videos.add(new Video("-q23NBO3LxI", "[NKHN Day47 _] Hannah Quỳnh \"lên tiếng\""));
        videos.add(new Video("qvX78b1sLfI", "[Hannah's Diary] #2 _ \"Chạm mặt\" hot girl mê CNTT"));
        videos.add(new Video("O6m2eK8B98M", "[NKHN Day44] Hannah thực địa xDay miền Nam"));
        videos.add(new Video("-EP56UdgwTA", "[Nhật ký Hannah Day11] xTer đánh đàn chào mừng ngày 20/11"));
        videos.add(new Video("IEKXl9awq8U", "[Nhật ký Hannah Day6] Địa bàn FUNiX"));
        videos.add(new Video("dy6_jDebIGg", "[Nhật ký Hannah Day4] Giới thiệu không gian xCafe"));


        return videos;
    }

}
