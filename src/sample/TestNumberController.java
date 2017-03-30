/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jxl.write.WriteException;

/**
 *
 * @author gsamadova
 */
public class TestNumberController implements Initializable {

    public WriteExcel writeExcel;

    public class Item {
        public SimpleIntegerProperty msisdn = new SimpleIntegerProperty();
        public SimpleStringProperty iccid = new SimpleStringProperty();
        public SimpleStringProperty status = new SimpleStringProperty();
        public SimpleStringProperty tp = new SimpleStringProperty();
        public SimpleStringProperty env = new SimpleStringProperty();
        public SimpleStringProperty name = new SimpleStringProperty();
        public SimpleStringProperty surname = new SimpleStringProperty();
        public SimpleStringProperty comment = new SimpleStringProperty();



        public Integer getMsisdn()  {return msisdn.get();}
        public String getIccid()    {return iccid.get();}
        public String getStatus()   {return status.get();}
        public String getTp()   {return tp.get();}
        public String getEnv()  {return env.get();}
        public String getName() {return name.get();}
        public String getSurname()  {return surname.get();}
        public String getComment()  {return comment.get();}
    }

    public class StringConverterNew extends StringConverter {
        @Override
        public String toString(Object t) {
            return t==null ? "" : t.toString();
        }
        @Override
        public Object fromString(String string) {
            return string;
        }
    }

    public class TextFieldTableCellNew extends TextFieldTableCell<Map, String>   {
        TextFieldTableCellNew(TableColumn<Map, String> p)   {
            new StringConverterNew();
        }
    }

    @FXML public TableView<Item> itemTbl;
    @FXML public TableColumn itemMsisdnCol, itemIccidCol, itemStatusCol;
    @FXML public TableColumn itemTpCol, itemEnvCol, itemCommentCol;
    @FXML public TableColumn itemNameCol, itemSurnameCol;
    @FXML public Label labelState;

    ObservableList<Item> data;
    ArrayList<Item> dataList;
    @FXML TextField searchByField;
    private DBClass objDbClass;
    Connection conProd, conTest;

    @FXML public CheckBox prodCheck;
    @FXML public CheckBox testCheck;
    @FXML public CheckBox report;

    @FXML public RadioButton rbNumber;
    @FXML public RadioButton rbSim;
    @FXML public RadioButton rbTp;
    @FXML public RadioButton rbComment;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataList = new ArrayList<>();
        itemTbl.setEditable(true);
        itemTbl.getSelectionModel().setCellSelectionEnabled(true);

        itemTbl.getColumns().setAll(
                itemMsisdnCol, itemIccidCol, itemStatusCol, itemTpCol,
                itemEnvCol, itemNameCol, itemSurnameCol, itemCommentCol);

        itemMsisdnCol.setCellValueFactory(new PropertyValueFactory<Item, Integer>("msisdn"));
        itemMsisdnCol.setCellFactory(new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn<Map, String> tableColumn) {
                return new TextFieldTableCellNew(tableColumn);
            }
        });
        itemMsisdnCol.setEditable(true);
        itemIccidCol.setCellValueFactory(new PropertyValueFactory<Item, String>("iccid"));
        itemIccidCol.setCellFactory(new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn<Map, String> tableColumn) {
                return new TextFieldTableCellNew(tableColumn);
            }
        });
        itemIccidCol.setEditable(true);
        itemStatusCol.setCellValueFactory(new PropertyValueFactory<Item, String>("status"));
        itemStatusCol.setCellFactory(new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn<Map, String> tableColumn) {
                return new TextFieldTableCellNew(tableColumn);
            }
        });
        itemStatusCol.setEditable(true);
        itemTpCol.setCellValueFactory(new PropertyValueFactory<Item, String>("tp"));
        itemTpCol.setCellFactory(new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn<Map, String> tableColumn) {
                return new TextFieldTableCellNew(tableColumn);
            }
        });
        itemTpCol.setEditable(true);
        itemEnvCol.setCellValueFactory(new PropertyValueFactory<Item, String>("env"));
        itemNameCol.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        itemNameCol.setCellFactory(new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn<Map, String> tableColumn) {
                return new TextFieldTableCellNew(tableColumn);
            }
        });
        itemNameCol.setEditable(true);
        itemSurnameCol.setCellValueFactory(new PropertyValueFactory<Item, String>("surname"));
        itemSurnameCol.setCellFactory(new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn<Map, String> tableColumn) {
                return new TextFieldTableCellNew(tableColumn);
            }
        });
        itemSurnameCol.setEditable(true);
        itemCommentCol.setCellValueFactory(new PropertyValueFactory<Item,String>("comment"));
        itemCommentCol.setCellFactory(new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn<Map, String> tableColumn) {
                return new TextFieldTableCellNew(tableColumn);
            }
        });
        itemCommentCol.setEditable(true);

        data = FXCollections.observableArrayList();
        itemTbl.setItems(data);
        objDbClass = new DBClass();
        try{
            conProd = objDbClass.getConnection();
            conTest = objDbClass.getConnectionTesBed();

            TestNumberV01.checker = new NetworkChecker();
            TestNumberV01.checkThread = new Thread(TestNumberV01.checker);
            TestNumberV01.checkThread.start();

        }
        catch(ClassNotFoundException | SQLException ce){
            Logger.getLogger(TestNumberController.class.getName()).log(Level.SEVERE, null, ce);
            labelState.setText("Could not establish \nthe connection");
        }

        final ToggleGroup toggleGroup = new ToggleGroup();
        rbNumber.setToggleGroup(toggleGroup);
        rbSim.setToggleGroup(toggleGroup);
        rbTp.setToggleGroup(toggleGroup);
        rbComment.setToggleGroup(toggleGroup);
    }

    public void buildData(int column, String value){
        PreparedStatement prepProd = null, prepTest = null;
        String SQL = null;
        String sqlTest = null;
        try {
            if(conTest.isClosed() || conProd.isClosed()) {
                labelState.setText("reconnection...");
                try{
                    conProd = objDbClass.getConnection();
                    conTest = objDbClass.getConnectionTesBed();
                }
                catch(ClassNotFoundException | SQLException ce){
                    Logger.getLogger(TestNumberController.class.getName()).log(Level.SEVERE, null, ce);
                    labelState.setText("Could not establish \nthe connection");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(TestNumberController.class.getName()).log(Level.SEVERE, null, e);
        }

        try{
            if(value.equals("") || value.isEmpty()) {
                if(prodCheck.isSelected()) {
                    SQL = "select SA.MSISDN as \"msisdn\",SA.ICCID as \"iccid\", INS.REMARK as \"status\",  MO.OFFER_NAME as \"tp\", " +
                            "initcap(IC.NAME1) as \"name\", initcap(IC.NAME3) as \"surname\", initcap(IC.NAME2) as \"comment\"\n" +
                            "from CCARE.INF_SUBSCRIBER_ALL sa,CCARE.INF_OFFERs io , CCARE.PDM_OFFER mo , CCARE.INF_STATUS ins, CCARE.INF_CUSTOMER_ALL ic, creation_num_new t  \n" +
                            "where IO.SUB_ID = SA.SUB_ID and IO.PRIMARY_FLAG=1 and IO.OFFER_ID=MO.OFFER_ID\n" +
                            "and ins.STATUS_ID=SA.SUB_STATE and IC.CUST_ID=SA.CUST_ID and sa.exp_date>sysdate and sa.msisdn =t.msisdn2 and io.eff_date<sysdate";
                    prepProd=conProd.prepareStatement(SQL);
                    prepProd.setQueryTimeout(1);
                }
                if(testCheck.isSelected()) {
                    sqlTest = "select SA.MSISDN \"msisdn\",SA.ICCID \"iccid\", INS.REMARK as \"status\",  MO.OFFER_NAME as \"tp\", " +
                            "initcap(IC.NAME1) as \"name\", initcap(IC.NAME3) as \"surname\", initcap(IC.NAME2) as \"comment\"\n" +
                            "from CCARE.INF_SUBSCRIBER_ALL sa,CCARE.INF_OFFERs io , CCARE.PDM_OFFER mo , CCARE.INF_STATUS ins, CCARE.INF_CUSTOMER_ALL ic, creation_num_testbed t  \n" +
                            "where IO.SUB_ID = SA.SUB_ID and IO.PRIMARY_FLAG=1 and IO.OFFER_ID=MO.OFFER_ID and ins.STATUS_ID=SA.SUB_STATE\n" +
                            "and IC.CUST_ID=SA.CUST_ID and sa.exp_date>sysdate and sa.msisdn =t.msisdn and io.eff_date<sysdate";
                    prepTest=conTest.prepareStatement(sqlTest);
                    prepTest.setQueryTimeout(1);
                }
            } else {
                switch(column)  {
                    case 1:
                        if(prodCheck.isSelected()) {
                            SQL = "select SA.MSISDN as \"msisdn\",SA.ICCID as \"iccid\", INS.REMARK as \"status\",  MO.OFFER_NAME as \"tp\", " +
                                    "initcap(IC.NAME1) as \"name\", initcap(IC.NAME3) as \"surname\", initcap(IC.NAME2) as \"comment\"\n" +
                                    "from CCARE.INF_SUBSCRIBER_ALL sa,CCARE.INF_OFFERs io , CCARE.PDM_OFFER mo , CCARE.INF_STATUS ins, CCARE.INF_CUSTOMER_ALL ic, creation_num_new t  \n" +
                                    "where sa.msisdn like ? and IO.SUB_ID = SA.SUB_ID and IO.PRIMARY_FLAG=1 and IO.OFFER_ID=MO.OFFER_ID\n" +
                                    "and ins.STATUS_ID=SA.SUB_STATE and IC.CUST_ID=SA.CUST_ID and sa.exp_date>sysdate and sa.msisdn =t.msisdn2 and io.eff_date<sysdate";
                            prepProd=conProd.prepareStatement(SQL);
                            //prepProd.setInt(1, Integer.parseInt(value));
                            prepProd.setString(1, "%"+value+"%");
                        }
                        if(testCheck.isSelected()) {
                            sqlTest = "select SA.MSISDN \"msisdn\",SA.ICCID \"iccid\", INS.REMARK as \"status\",  MO.OFFER_NAME as \"tp\", " +
                                    "initcap(IC.NAME1) as \"name\", initcap(IC.NAME3) as \"surname\", initcap(IC.NAME2) as \"comment\"\n" +
                                    "from CCARE.INF_SUBSCRIBER_ALL sa,CCARE.INF_OFFERs io , CCARE.PDM_OFFER mo , CCARE.INF_STATUS ins, CCARE.INF_CUSTOMER_ALL ic, creation_num_testbed t  \n" +
                                    "where sa.msisdn like ? and IO.SUB_ID = SA.SUB_ID and IO.PRIMARY_FLAG=1 and IO.OFFER_ID=MO.OFFER_ID and ins.STATUS_ID=SA.SUB_STATE\n" +
                                    "and IC.CUST_ID=SA.CUST_ID and sa.exp_date>sysdate and sa.msisdn =t.msisdn and io.eff_date<sysdate";
                            prepTest=conTest.prepareCall(sqlTest);
                            //prepProd.setInt(1, Integer.parseInt(value));
                            prepTest.setString(1, "%"+value+"%");
                        }

                        break;
                    case 2:
                        if(prodCheck.isSelected()) {
                            SQL ="select SA.MSISDN as \"msisdn\",SA.ICCID as \"iccid\", INS.REMARK as \"status\",  MO.OFFER_NAME as \"tp\", " +
                                    "initcap(IC.NAME1) as \"name\", initcap(IC.NAME3) as \"surname\", initcap(IC.NAME2) as \"comment\"\n" +
                                    "from CCARE.INF_SUBSCRIBER_ALL sa,CCARE.INF_OFFERs io , CCARE.PDM_OFFER mo , CCARE.INF_STATUS ins, CCARE.INF_CUSTOMER_ALL ic, creation_num_new t  \n" +
                                    "where sa.iccid like ? and IO.SUB_ID = SA.SUB_ID and IO.PRIMARY_FLAG=1 and IO.OFFER_ID=MO.OFFER_ID\n" +
                                    "and ins.STATUS_ID=SA.SUB_STATE and IC.CUST_ID=SA.CUST_ID and sa.exp_date>sysdate and sa.msisdn =t.msisdn2 and io.eff_date<sysdate";
                            prepProd=conProd.prepareStatement(SQL);
                            prepProd.setString(1, "%"+value+"%");
                        }
                        if(testCheck.isSelected()) {
                            sqlTest="select SA.MSISDN \"msisdn\",SA.ICCID \"iccid\", INS.REMARK as \"status\",  MO.OFFER_NAME as \"tp\", " +
                                    "initcap(IC.NAME1) as \"name\", initcap(IC.NAME3) as \"surname\", initcap(IC.NAME2) as \"comment\"\n" +
                                    "from CCARE.INF_SUBSCRIBER_ALL sa,CCARE.INF_OFFERs io , CCARE.PDM_OFFER mo , CCARE.INF_STATUS ins, CCARE.INF_CUSTOMER_ALL ic, creation_num_testbed t  \n" +
                                    "where sa.iccid like ? and IO.SUB_ID = SA.SUB_ID and IO.PRIMARY_FLAG=1 and IO.OFFER_ID=MO.OFFER_ID and ins.STATUS_ID=SA.SUB_STATE\n" +
                                    "and IC.CUST_ID=SA.CUST_ID and sa.exp_date>sysdate and sa.msisdn =t.msisdn and io.eff_date<sysdate";
                            prepTest=conTest.prepareStatement(sqlTest);
                            prepTest.setString(1, "%" + value + "%");
                        }

                        break;
                    case 3:
                        if(prodCheck.isSelected()) {
                            SQL ="select SA.MSISDN as \"msisdn\",SA.ICCID as \"iccid\", INS.REMARK as \"status\", MO.OFFER_NAME as \"tp\", " +
                                    "initcap(IC.NAME1) as \"name\", initcap(IC.NAME3) as \"surname\", initcap(IC.NAME2) as \"comment\"\n" +
                                    "from CCARE.INF_SUBSCRIBER_ALL sa,CCARE.INF_OFFERs io , CCARE.PDM_OFFER mo , CCARE.INF_STATUS ins, CCARE.INF_CUSTOMER_ALL ic, creation_num_new t  \n" +
                                    "where lower(mo.offer_name) like lower(?) and IO.SUB_ID = SA.SUB_ID and IO.PRIMARY_FLAG=1 and IO.OFFER_ID=MO.OFFER_ID\n" +
                                    "and ins.STATUS_ID=SA.SUB_STATE and IC.CUST_ID=SA.CUST_ID and sa.exp_date>sysdate and sa.msisdn =t.msisdn2 and io.eff_date<sysdate";
                            prepProd=conProd.prepareStatement(SQL);
                            prepProd.setString(1, "%"+value+"%");
                        }
                        if(testCheck.isSelected()) {
                            sqlTest="select SA.MSISDN \"msisdn\",SA.ICCID \"iccid\", INS.REMARK as \"status\", MO.OFFER_NAME as \"tp\", " +
                                    "initcap(IC.NAME1) as \"name\", initcap(IC.NAME3) as \"surname\", initcap(IC.NAME2) as \"comment\"\n" +
                                    "from CCARE.INF_SUBSCRIBER_ALL sa,CCARE.INF_OFFERs io , CCARE.PDM_OFFER mo , CCARE.INF_STATUS ins, CCARE.INF_CUSTOMER_ALL ic, creation_num_testbed t  \n" +
                                    "where lower(mo.offer_name) like lower(?) and IO.SUB_ID = SA.SUB_ID and IO.PRIMARY_FLAG=1 and IO.OFFER_ID=MO.OFFER_ID and ins.STATUS_ID=SA.SUB_STATE\n" +
                                    "and IC.CUST_ID=SA.CUST_ID and sa.exp_date>sysdate and sa.msisdn =t.msisdn and io.eff_date<sysdate";
                            prepTest=conTest.prepareStatement(sqlTest);
                            prepTest.setString(1, "%"+value+"%");
                        }

                        break;
                    case 4:
                        if(prodCheck.isSelected()) {
                            SQL ="select SA.MSISDN as \"msisdn\",SA.ICCID as \"iccid\", INS.REMARK as \"status\", MO.OFFER_NAME as \"tp\", " +
                                    "initcap(IC.NAME1) as \"name\", initcap(IC.NAME3) as \"surname\", initcap(IC.NAME2) as \"comment\"\n" +
                                    "from CCARE.INF_SUBSCRIBER_ALL sa,CCARE.INF_OFFERs io , CCARE.PDM_OFFER mo , CCARE.INF_STATUS ins, CCARE.INF_CUSTOMER_ALL ic, creation_num_new t  \n" +
                                    "where lower(ic.name1||ic.name2||ic.name3) like lower(?) and IO.SUB_ID = SA.SUB_ID and IO.PRIMARY_FLAG=1 and IO.OFFER_ID=MO.OFFER_ID and ins.STATUS_ID=SA.SUB_STATE\n" +
                                    "and IC.CUST_ID=SA.CUST_ID and sa.exp_date>sysdate and sa.msisdn =t.msisdn2 and io.eff_date<sysdate";
                            prepProd=conProd.prepareStatement(SQL);
                            prepProd.setString(1, "%"+value.toLowerCase()+"%");
                        }
                        if(testCheck.isSelected()) {
                            sqlTest="select SA.MSISDN \"msisdn\",SA.ICCID \"iccid\", INS.REMARK as \"status\", MO.OFFER_NAME as \"tp\", " +
                                    "initcap(IC.NAME1) as \"name\", initcap(IC.NAME3) as \"surname\", initcap(IC.NAME2) as \"comment\"\n" +
                                    "from CCARE.INF_SUBSCRIBER_ALL sa,CCARE.INF_OFFERs io , CCARE.PDM_OFFER mo , CCARE.INF_STATUS ins, CCARE.INF_CUSTOMER_ALL ic, creation_num_testbed t  \n" +
                                    "where lower(ic.name1||ic.name2||ic.name3) like lower(?)and IO.SUB_ID = SA.SUB_ID and IO.PRIMARY_FLAG=1 and IO.OFFER_ID=MO.OFFER_ID and ins.STATUS_ID=SA.SUB_STATE\n" +
                                    "and IC.CUST_ID=SA.CUST_ID and sa.exp_date>sysdate and sa.msisdn =t.msisdn and io.eff_date<sysdate";
                            prepTest=conTest.prepareStatement(sqlTest);
                            prepTest.setString(1, "%"+value.toLowerCase()+"%");
                        }
                        break;
                    default: break;
                }
                prepProd.setQueryTimeout(1);
                prepTest.setQueryTimeout(1);
            }

            ResultSet rs, rsTest;
            ArrayList<Integer> activeNumbers = new ArrayList<>();
            int num;

            if(report.isSelected()) {
                rs = prepProd.executeQuery();
                rsTest = prepTest.executeQuery();
                writeExcel = new WriteExcel();
                String filePath = new File("").getAbsolutePath();
                filePath=filePath.concat("\\report\\report.xls");
                System.out.println(filePath);
//                writeExcel.setOutputFile("C:\\Users\\gsamadova\\Desktop\\QAtool\\report\\report.xls");
                writeExcel.setOutputFile(filePath);
                try {
                    writeExcel.write(rs, rsTest);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }

            try {
                if(prodCheck.isSelected())  {


                    rs = prepProd.executeQuery();
                    while(rs.next()){
                        Item item = new Item();
                        item.msisdn.setValue(Integer.parseInt(rs.getString("msisdn")));
                        item.iccid.setValue(rs.getString("iccid"));
                        item.status.setValue(rs.getString("status"));
                        item.tp.setValue(rs.getString("tp"));
                        item.env.setValue("P");
                        item.name.setValue(rs.getString("name"));
                        item.surname.setValue(rs.getString("surname"));
                        item.comment.setValue(rs.getString("comment"));

                        if(!Utils.doesContain(dataList, item))    {
                            dataList.add(item);
                            data.add(item);
                        }

                        num = Integer.parseInt(rs.getString("msisdn"));
                        if(!activeNumbers.contains(num)) activeNumbers.add(num);
                    }
                }

                if(testCheck.isSelected())  {


                    rsTest = prepTest.executeQuery();
                    while(rsTest.next()){
                        Item item = new Item();
                        item.msisdn.setValue(Integer.parseInt(rsTest.getString("msisdn")));
                        item.iccid.setValue(rsTest.getString("iccid"));
                        item.status.setValue(rsTest.getString("status"));
                        item.tp.setValue(rsTest.getString("tp"));
                        item.env.setValue("T");
                        item.name.setValue(rsTest.getString("name"));
                        item.surname.setValue(rsTest.getString("surname"));
                        item.comment.setValue(rsTest.getString("comment"));

                        if(!Utils.doesContain(dataList, item))    {
                            dataList.add(item);
                            data.add(item);
                        }

                        num = Integer.parseInt(rsTest.getString("msisdn"));
                        if(!activeNumbers.contains(num)) activeNumbers.add(num);
                    }
                }

                labelState.setText("ready");
            } catch (SQLRecoverableException exception) {
                labelState.setText("lost connection");
            }




            if(prodCheck.isSelected() && testCheck.isSelected() && (value.equals("") || value.isEmpty()))  {

                ArrayList<Integer> deactiveNumbers = new ArrayList<>();
                deactiveNumbers = Utils.findDeactive(activeNumbers);

                for(Integer deactN : deactiveNumbers)   {

                    Item item = new Item();
                    item.msisdn.setValue(deactN);
                    item.env.setValue("N");
                    item.comment.setValue("Not found");

                    if(!Utils.doesContain(dataList, item))    {
                        dataList.add(item);
                        data.add(item);
                        System.out.println(item.msisdn.getValue());
                    }
                }
                deactiveNumbers.clear();
            }
            conProd.close();
            conTest.close();
        }
        catch(SQLException e){
            Logger.getLogger(TestNumberController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void onEnter(ActionEvent event) {
        labelState.setText("loading...");
        data.clear();
        dataList.clear();

        if(Utils.online)    {
            if(Utils.isNumeric(searchByField.getText().toString())) {
                buildData(1, searchByField.getText().toString());
                buildData(2, searchByField.getText().toString());
            } else {
                buildData(3, searchByField.getText().toString());
                buildData(4, searchByField.getText().toString());
            }
            labelState.setText("Ready");
        } else {
            labelState.setText("Connection lost");
        }
    }

    @FXML
    private void onReset(ActionEvent event) {
        data.clear();
        rbNumber.setSelected(true);
        prodCheck.setSelected(true);
        testCheck.setSelected(true);
        searchByField.setText("");
    }
}