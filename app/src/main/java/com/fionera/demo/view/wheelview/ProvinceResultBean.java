package com.fionera.demo.view.wheelview;

import java.util.List;

/**
 * Created by fionera on 17-1-16 in AndroidDemo.
 */

public class ProvinceResultBean {

    private List<ProvinceModel> provinceModelList;

    public List<ProvinceModel> getProvinceModelList() {
        return provinceModelList;
    }

    public void setProvinceModelList(List<ProvinceModel> provinceModelList) {
        this.provinceModelList = provinceModelList;
    }

    public static class ProvinceModel {
        /**
         * name : 测试
         * cityList : [{"name":"城市","districtList":[{"name":"枞阳县","zipCode":"246000"},
         * {"name":"大观区","zipCode":"246000"},{"name":"怀宁县","zipCode":"246000"},{"name":"潜山县",
         * "zipCode":"246000"},{"name":"宿松县","zipCode":"246000"},{"name":"太湖县",
         * "zipCode":"246000"},{"name":"桐城市","zipCode":"246000"},{"name":"望江县",
         * "zipCode":"246000"},{"name":"宜秀区","zipCode":"246000"},{"name":"迎江区",
         * "zipCode":"246000"},{"name":"岳西县","zipCode":"246000"},{"name":"其他","zipCode":"246000"}]}]
         */

        private String name;
        private List<CityModel> cityList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CityModel> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityModel> cityList) {
            this.cityList = cityList;
        }

        public static class CityModel {
            /**
             * name : 城市
             * districtList : [{"name":"枞阳县","zipCode":"246000"},{"name":"大观区",
             * "zipCode":"246000"},{"name":"怀宁县","zipCode":"246000"},{"name":"潜山县",
             * "zipCode":"246000"},{"name":"宿松县","zipCode":"246000"},{"name":"太湖县",
             * "zipCode":"246000"},{"name":"桐城市","zipCode":"246000"},{"name":"望江县",
             * "zipCode":"246000"},{"name":"宜秀区","zipCode":"246000"},{"name":"迎江区",
             * "zipCode":"246000"},{"name":"岳西县","zipCode":"246000"},{"name":"其他",
             * "zipCode":"246000"}]
             */

            private String name;
            private List<DistrictModel> districtList;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<DistrictModel> getDistrictList() {
                return districtList;
            }

            public void setDistrictList(List<DistrictModel> districtList) {
                this.districtList = districtList;
            }

            public static class DistrictModel {
                /**
                 * name : 枞阳县
                 * zipCode : 246000
                 */

                private String name;
                private String zipCode;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getZipCode() {
                    return zipCode;
                }

                public void setZipCode(String zipCode) {
                    this.zipCode = zipCode;
                }
            }
        }
    }
}
