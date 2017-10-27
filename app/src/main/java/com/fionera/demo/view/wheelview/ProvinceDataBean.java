package com.fionera.demo.view.wheelview;

import java.util.List;

/**
 * @author fionera
 */
public class ProvinceDataBean {

    private List<ProvinceListEntity> provinceList;

    public List<ProvinceListEntity> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<ProvinceListEntity> provinceList) {
        this.provinceList = provinceList;
    }

    public static class ProvinceListEntity {
        /**
         * provinceId : 1
         * provinceName : 北京
         * cityList : [{"cityId":"36","cityName":"北京市","areaList":[{"areaId":"37",
         * "areaName":"东城区"}]}]
         */

        private String provinceId;
        private String provinceName;
        private List<CityListEntity> cityList;

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public List<CityListEntity> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityListEntity> cityList) {
            this.cityList = cityList;
        }

        public static class CityListEntity {
            /**
             * cityId : 36
             * cityName : 北京市
             * areaList : [{"areaId":"37","areaName":"东城区"}]
             */

            private String cityId;
            private String cityName;
            private List<AreaListEntity> areaList;

            public String getCityId() {
                return cityId;
            }

            public void setCityId(String cityId) {
                this.cityId = cityId;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public List<AreaListEntity> getAreaList() {
                return areaList;
            }

            public void setAreaList(List<AreaListEntity> areaList) {
                this.areaList = areaList;
            }

            public static class AreaListEntity {
                /**
                 * areaId : 37
                 * areaName : 东城区
                 */

                private String areaId;
                private String areaName;

                public String getAreaId() {
                    return areaId;
                }

                public void setAreaId(String areaId) {
                    this.areaId = areaId;
                }

                public String getAreaName() {
                    return areaName;
                }

                public void setAreaName(String areaName) {
                    this.areaName = areaName;
                }
            }
        }
    }
}
