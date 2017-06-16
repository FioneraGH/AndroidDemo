package com.fionera.demo.view.wheelview;

import java.util.List;

public class ProvinceDataBean {

    private List<ProvinceListEntity> province_list;

    public List<ProvinceListEntity> getProvince_list() {
        return province_list;
    }

    public void setProvince_list(List<ProvinceListEntity> province_list) {
        this.province_list = province_list;
    }

    public static class ProvinceListEntity {
        /**
         * province_id : 1
         * province_name : 北京
         * city_list : [{"city_id":"36","city_name":"北京市","area_list":[{"area_id":"37",
         * "area_name":"东城区"}]}]
         */

        private String province_id;
        private String province_name;
        private List<CityListEntity> city_list;

        public String getProvince_id() {
            return province_id;
        }

        public void setProvince_id(String province_id) {
            this.province_id = province_id;
        }

        public String getProvince_name() {
            return province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public List<CityListEntity> getCity_list() {
            return city_list;
        }

        public void setCity_list(List<CityListEntity> city_list) {
            this.city_list = city_list;
        }

        public static class CityListEntity {
            /**
             * city_id : 36
             * city_name : 北京市
             * area_list : [{"area_id":"37","area_name":"东城区"}]
             */

            private String city_id;
            private String city_name;
            private List<AreaListEntity> area_list;

            public String getCity_id() {
                return city_id;
            }

            public void setCity_id(String city_id) {
                this.city_id = city_id;
            }

            public String getCity_name() {
                return city_name;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
            }

            public List<AreaListEntity> getArea_list() {
                return area_list;
            }

            public void setArea_list(List<AreaListEntity> area_list) {
                this.area_list = area_list;
            }

            public static class AreaListEntity {
                /**
                 * area_id : 37
                 * area_name : 东城区
                 */

                private String area_id;
                private String area_name;

                public String getArea_id() {
                    return area_id;
                }

                public void setArea_id(String area_id) {
                    this.area_id = area_id;
                }

                public String getArea_name() {
                    return area_name;
                }

                public void setArea_name(String area_name) {
                    this.area_name = area_name;
                }
            }
        }
    }
}
