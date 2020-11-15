package com.example.wowtime.dto;

public class OptionGameItem {
    private String opt;
    private String statement;

    public OptionGameItem() {}
    public OptionGameItem(String opt, String statement) {
        this.opt = opt;
        this.statement = statement;
    }

    public String getOpt() {return opt;}
    public void setOpt(String opt) {this.opt = opt;}
    public String getStatement() {return statement;}
    public void setStatement(String statement) {this.statement = statement;}
}