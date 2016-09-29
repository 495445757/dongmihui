package com.dongmihui.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */

public class NewBean {
    @Override
    public String toString() {
        return "NewBean{" +
                "banner=" + banner +
                ", zixun=" + zixun +
                '}';
    }

    /**
         * id : 1
         * url : http://192.168.2.3/
         * pic : http://192.168.2.3/uploads/flash/1609261642021965.jpg
         */

        private List<BannerBean> banner;
        /**
         * id : 114
         * title :  国有企业结构调整基金成立
         * litpic : [{"pic":"http://192.168.2.3/uploads/allimg/c160927/14J95954910530-13191_lit.jpg"},{"pic":"http://192.168.2.3/uploads/allimg/c160927/14J95954910530-13191_lit.jpg"},{"pic":"http://192.168.2.3/uploads/allimg/c160927/14J95954910530-13191_lit.jpg"}]
         * type : 2
         * content : 国有企业结构调整基金将重点投资于中央企业产业结构调整。 IC 资料预计总规模为3
         */

        private List<ZixunBean> zixun;

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<ZixunBean> getZixun() {
            return zixun;
        }

        public void setZixun(List<ZixunBean> zixun) {
            this.zixun = zixun;
        }

        public static class BannerBean {
            private int id;
            private String url;
            private String pic;

            @Override
            public String toString() {
                return "BannerBean{" +
                        "id=" + id +
                        ", url='" + url + '\'' +
                        ", pic='" + pic + '\'' +
                        '}';
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }

        public static class ZixunBean {
            private int id;
            private String title;
            private int type;
            private String content;
            /**
             * pic : http://192.168.2.3/uploads/allimg/c160927/14J95954910530-13191_lit.jpg
             */

            private List<LitpicBean> litpic;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public List<LitpicBean> getLitpic() {
                return litpic;
            }

            public void setLitpic(List<LitpicBean> litpic) {
                this.litpic = litpic;
            }

            public static class LitpicBean {
                private String pic;

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }
            }
        }
}
