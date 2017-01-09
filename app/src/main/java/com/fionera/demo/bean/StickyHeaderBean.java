package com.fionera.demo.bean;

import java.util.List;

/**
 * Created by fionera on 17-1-9 in AndroidDemo.
 */

public class StickyHeaderBean {

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {

        private String stid;
        private List<ComingEntity> coming;
        private List<?> hot;
        private List<Integer> movieIds;

        public String getStid() {
            return stid;
        }

        public void setStid(String stid) {
            this.stid = stid;
        }

        public List<ComingEntity> getComing() {
            return coming;
        }

        public void setComing(List<ComingEntity> coming) {
            this.coming = coming;
        }

        public List<?> getHot() {
            return hot;
        }

        public void setHot(List<?> hot) {
            this.hot = hot;
        }

        public List<Integer> getMovieIds() {
            return movieIds;
        }

        public void setMovieIds(List<Integer> movieIds) {
            this.movieIds = movieIds;
        }

        public static class ComingEntity {
            /**
             * boxInfo : 喵，即将上映
             * cat : 爱情,冒险,科幻
             * civilPubSt : 0
             * comingTitle : 1月13日 周五
             * desc : 主演:詹妮弗·劳伦斯,克里斯·帕拉特,迈克尔·辛
             * dir : 莫腾·泰杜姆
             * dur : 118
             * effectShowNum : 0
             * fra : 美国
             * frt : 2016-12-21
             * globalReleased : false
             * headLineShow : false
             * headLinesVO : []
             * id : 341201
             * img : http://p0.meituan.net/w.h/movie/9b7f7df143af8a13ccd17057d772e9e7654933.png
             * late : false
             * localPubSt : 0
             * mk : 0
             * nm : 太空旅客
             * pn : 165
             * preShow : true
             * proScore : 0
             * proScoreNum : 0
             * pubDate : 1484236800000
             * pubDesc : 2017-01-13大陆上映
             * pubShowNum : 0
             * recentShowDate : 0
             * recentShowNum : 0
             * rt : 2017-01-13
             * sc : 0
             * scm : 未达目的地，太空铁达尼
             * showCinemaNum : 0
             * showInfo : 2017-01-13 本周五上映
             * showNum : 0
             * showst : 4
             * snum : 3262
             * star : 詹妮弗·劳伦斯,克里斯·帕拉特,迈克尔·辛
             * ver : 3D/中国巨幕
             * videoId : 82847
             * videoName : “极致冒险”版预告片
             * videourl : http://maoyan.meituan
             * .net/movie/videos/854x4804f1ae831eb464875901093fbb1a2eda7.mp4
             * vnum : 23
             * weight : 1
             * wish : 40022
             * wishst : 0
             */

            private String boxInfo;
            private String cat;
            private int civilPubSt;
            private String comingTitle;
            private String desc;
            private String dir;
            private int dur;
            private int effectShowNum;
            private String fra;
            private String frt;
            private boolean globalReleased;
            private boolean headLineShow;
            private int id;
            private String img;
            private boolean late;
            private int localPubSt;
            private int mk;
            private String nm;
            private int pn;
            private boolean preShow;
            private int proScore;
            private int proScoreNum;
            private long pubDate;
            private String pubDesc;
            private int pubShowNum;
            private int recentShowDate;
            private int recentShowNum;
            private String rt;
            private int sc;
            private String scm;
            private int showCinemaNum;
            private String showInfo;
            private int showNum;
            private int showst;
            private int snum;
            private String star;
            private String ver;
            private int videoId;
            private String videoName;
            private String videourl;
            private int vnum;
            private int weight;
            private int wish;
            private int wishst;
            private List<?> headLinesVO;

            public String getBoxInfo() {
                return boxInfo;
            }

            public void setBoxInfo(String boxInfo) {
                this.boxInfo = boxInfo;
            }

            public String getCat() {
                return cat;
            }

            public void setCat(String cat) {
                this.cat = cat;
            }

            public int getCivilPubSt() {
                return civilPubSt;
            }

            public void setCivilPubSt(int civilPubSt) {
                this.civilPubSt = civilPubSt;
            }

            public String getComingTitle() {
                return comingTitle;
            }

            public void setComingTitle(String comingTitle) {
                this.comingTitle = comingTitle;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public int getDur() {
                return dur;
            }

            public void setDur(int dur) {
                this.dur = dur;
            }

            public int getEffectShowNum() {
                return effectShowNum;
            }

            public void setEffectShowNum(int effectShowNum) {
                this.effectShowNum = effectShowNum;
            }

            public String getFra() {
                return fra;
            }

            public void setFra(String fra) {
                this.fra = fra;
            }

            public String getFrt() {
                return frt;
            }

            public void setFrt(String frt) {
                this.frt = frt;
            }

            public boolean isGlobalReleased() {
                return globalReleased;
            }

            public void setGlobalReleased(boolean globalReleased) {
                this.globalReleased = globalReleased;
            }

            public boolean isHeadLineShow() {
                return headLineShow;
            }

            public void setHeadLineShow(boolean headLineShow) {
                this.headLineShow = headLineShow;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public boolean isLate() {
                return late;
            }

            public void setLate(boolean late) {
                this.late = late;
            }

            public int getLocalPubSt() {
                return localPubSt;
            }

            public void setLocalPubSt(int localPubSt) {
                this.localPubSt = localPubSt;
            }

            public int getMk() {
                return mk;
            }

            public void setMk(int mk) {
                this.mk = mk;
            }

            public String getNm() {
                return nm;
            }

            public void setNm(String nm) {
                this.nm = nm;
            }

            public int getPn() {
                return pn;
            }

            public void setPn(int pn) {
                this.pn = pn;
            }

            public boolean isPreShow() {
                return preShow;
            }

            public void setPreShow(boolean preShow) {
                this.preShow = preShow;
            }

            public int getProScore() {
                return proScore;
            }

            public void setProScore(int proScore) {
                this.proScore = proScore;
            }

            public int getProScoreNum() {
                return proScoreNum;
            }

            public void setProScoreNum(int proScoreNum) {
                this.proScoreNum = proScoreNum;
            }

            public long getPubDate() {
                return pubDate;
            }

            public void setPubDate(long pubDate) {
                this.pubDate = pubDate;
            }

            public String getPubDesc() {
                return pubDesc;
            }

            public void setPubDesc(String pubDesc) {
                this.pubDesc = pubDesc;
            }

            public int getPubShowNum() {
                return pubShowNum;
            }

            public void setPubShowNum(int pubShowNum) {
                this.pubShowNum = pubShowNum;
            }

            public int getRecentShowDate() {
                return recentShowDate;
            }

            public void setRecentShowDate(int recentShowDate) {
                this.recentShowDate = recentShowDate;
            }

            public int getRecentShowNum() {
                return recentShowNum;
            }

            public void setRecentShowNum(int recentShowNum) {
                this.recentShowNum = recentShowNum;
            }

            public String getRt() {
                return rt;
            }

            public void setRt(String rt) {
                this.rt = rt;
            }

            public int getSc() {
                return sc;
            }

            public void setSc(int sc) {
                this.sc = sc;
            }

            public String getScm() {
                return scm;
            }

            public void setScm(String scm) {
                this.scm = scm;
            }

            public int getShowCinemaNum() {
                return showCinemaNum;
            }

            public void setShowCinemaNum(int showCinemaNum) {
                this.showCinemaNum = showCinemaNum;
            }

            public String getShowInfo() {
                return showInfo;
            }

            public void setShowInfo(String showInfo) {
                this.showInfo = showInfo;
            }

            public int getShowNum() {
                return showNum;
            }

            public void setShowNum(int showNum) {
                this.showNum = showNum;
            }

            public int getShowst() {
                return showst;
            }

            public void setShowst(int showst) {
                this.showst = showst;
            }

            public int getSnum() {
                return snum;
            }

            public void setSnum(int snum) {
                this.snum = snum;
            }

            public String getStar() {
                return star;
            }

            public void setStar(String star) {
                this.star = star;
            }

            public String getVer() {
                return ver;
            }

            public void setVer(String ver) {
                this.ver = ver;
            }

            public int getVideoId() {
                return videoId;
            }

            public void setVideoId(int videoId) {
                this.videoId = videoId;
            }

            public String getVideoName() {
                return videoName;
            }

            public void setVideoName(String videoName) {
                this.videoName = videoName;
            }

            public String getVideourl() {
                return videourl;
            }

            public void setVideourl(String videourl) {
                this.videourl = videourl;
            }

            public int getVnum() {
                return vnum;
            }

            public void setVnum(int vnum) {
                this.vnum = vnum;
            }

            public int getWeight() {
                return weight;
            }

            public void setWeight(int weight) {
                this.weight = weight;
            }

            public int getWish() {
                return wish;
            }

            public void setWish(int wish) {
                this.wish = wish;
            }

            public int getWishst() {
                return wishst;
            }

            public void setWishst(int wishst) {
                this.wishst = wishst;
            }

            public List<?> getHeadLinesVO() {
                return headLinesVO;
            }

            public void setHeadLinesVO(List<?> headLinesVO) {
                this.headLinesVO = headLinesVO;
            }
        }
    }
}
