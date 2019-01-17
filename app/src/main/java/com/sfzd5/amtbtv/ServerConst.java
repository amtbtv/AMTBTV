package com.sfzd5.amtbtv;

public class ServerConst {

    /**
     *不好意思，下面還有兩處修改(amtbsg.cloudapp.net申請不了https證書，需要替換域名）
     *
     * A、文檔裡的下面這個獲取bg和card圖片地址域名有修改，也改成https了
     * 四、bg和card圖片地址
     * https://vod.hwadzan.com/redirect/v/amtbtv/pic/ 開頭
     * 例子
     * https://vod.hwadzan.com/redirect/v/amtbtv/pic/02-037_bg.jpg
     * https://vod.hwadzan.com/redirect/v/amtbtv/pic/02-037_card.jpg
     *
     * B、獲取最優主機的也要修改為上面的域名 AMTBTV/app/src/main/java/com/sfzd5/amtbtv/page/VideoPlayerFragment.java
     *
     * URL = "http://amtbsg.cloudapp.net/redirect/vod/_definst_/mp4/" + us[0] + "/" + history.identifier + "/" + file + "/playlist.m3u8";
     * 也請修改為
     * URL = "https://vod.hwadzan.com/redirect/vod/_definst_/mp4/" + us[0] + "/" + history.identifier + "/" + file + "/playlist.m3u8";
     */
    //{"channels":[{"name":"\u963f\u5f4c\u9640\u7d93","amtbid":"1"},{"name":"\u7121\u91cf\u58fd\u7d93","amtbid":"2"}]}
    private static final String liveChannelsUrl  = "https://amtbapi.hwadzan.com/amtbtv/channels/live,mp4";

    //{"files":["02-012-0001.mp4","02-012-0002.mp4"]}
    private static final String filesUrl  = "https://amtbapi.hwadzan.com/amtbtv/%s/mp4";

    //{"programs":[{"name":"\u7121\u91cf\u58fd\u7d93\u5927\u610f","identifier":"02-002","recDate":"1992.12","recAddress":"\u7f8e\u570b","picCreated":"1","mp4":"1","mp3":"1"}]}
    private static final String programsUrl  = "https://amtbapi.hwadzan.com/amtbtv/%d/mp4";

    //最近视频，暂时先不使用此功能
    private static final String newMediasUrl = "https://amtbapi.hwadzan.com/amtbtv/newmedias/mp4?limit=20";

    private static final String picUrl = "https://vod.hwadzan.com/redirect/v/amtbtv/pic/";

    public static String getProgramVideoUrl(String identifier, String file){
        String[] us = identifier.split("-");
        //return "http://amtbsg.cloudapp.net/redirect/vod/_definst_/mp4/" + us[0] + "/" + identifier + "/" + file + "/playlist.m3u8";
        return "https://vod.hwadzan.com/redirect/vod/_definst_/mp4/" + us[0] + "/" + identifier + "/" + file + "/playlist.m3u8";
    }

    public static String takeLiveChannelsUrl(){
        return liveChannelsUrl;
    }

    public static String takeFilesUrl(String identifier){
        return String.format(filesUrl, identifier);
    }

    public static String takeProgramsUrl(int amtbid){
        return String.format(programsUrl, amtbid);
    }

    public static String getProgramCardPic(String identifier) {
        //https://vod.hwadzan.com/redirect/v/amtbtv/pic/02-037_card.jpg
        //return "http://amtbsg.cloudapp.net/redirect/v/amtbtv/pic/" + identifier + "_card.jpg";
        return picUrl + identifier + "_card.jpg";
    }

    public static String getProgramBgPic(String identifier) {
        //https://vod.hwadzan.com/redirect/v/amtbtv/pic/02-037_bg.jpg
        //return "http://amtbsg.cloudapp.net/redirect/v/amtbtv/pic/" + identifier + "_bg.jpg";
        return picUrl + identifier + "_bg.jpg";
    }

    public static String getLiveCardPic(String cardPic) {
        return picUrl + cardPic;
    }

    public static String getLiveBgPic(String bgPic) {
        return picUrl + bgPic;
    }
}
