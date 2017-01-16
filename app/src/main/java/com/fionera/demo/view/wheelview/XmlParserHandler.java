package com.fionera.demo.view.wheelview;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XmlParserHandler
        extends DefaultHandler {

    private List<ProvinceResultBean.ProvinceModel> provinceList = new ArrayList<>();

    private ProvinceResultBean.ProvinceModel provinceModel;
    private ProvinceResultBean.ProvinceModel.CityModel cityModel;
    private ProvinceResultBean.ProvinceModel.CityModel.DistrictModel districtModel;

    public XmlParserHandler() {

    }

    public List<ProvinceResultBean.ProvinceModel> getDataList() {
        return provinceList;
    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        switch (qName) {
            case "province":
                provinceModel = new ProvinceResultBean.ProvinceModel();
                provinceModel.setName(attributes.getValue(0));
                provinceModel.setCityList(new ArrayList<>());
                break;
            case "city":
                cityModel = new ProvinceResultBean.ProvinceModel.CityModel();
                cityModel.setName(attributes.getValue(0));
                cityModel.setDistrictList(new ArrayList<>());
                break;
            case "district":
                districtModel = new ProvinceResultBean.ProvinceModel.CityModel.DistrictModel();
                districtModel.setName(attributes.getValue(0));
                districtModel.setZipCode(attributes.getValue(1));
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "district":
                cityModel.getDistrictList().add(districtModel);
                break;
            case "city":
                provinceModel.getCityList().add(cityModel);
                break;
            case "province":
                provinceList.add(provinceModel);
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

    }
}