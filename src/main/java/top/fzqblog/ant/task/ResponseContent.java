package top.fzqblog.ant.task;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * 数据内容
 * 可转成任何类型的数据
 */
public class ResponseContent {
    private byte[] contentBytes;
    private String content;
    private String charset;
    private Document document;

    public ResponseContent() {}

    public ResponseContent(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public byte[] bytes() {
        return contentBytes;
    }

    public String string() throws UnsupportedEncodingException {
        if (null == this.content) {
            if (null != this.charset) {
                this.content = new String(this.contentBytes, this.charset);
            } else {
                this.content = new String(this.contentBytes);
            }
        }
        return this.content;
    }

    public Document toDocument() throws IOException {
        if(this.document == null){
            this.document = Jsoup.parse(Optional.ofNullable(this.string()).orElse(""));
        }
        return this.document;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public ResponseContent charset(String charset) {
        this.charset = charset;
        return this;
    }

    public JSONObject toJsonObject() throws UnsupportedEncodingException {
        return JSONObject.parseObject(this.string());
    }

    public JSONArray toJsonArray() throws UnsupportedEncodingException {
        return JSONArray.parseArray(this.string());
    }

    @Override
    public String toString() {
        try {
            return "ResponseContent{" +
                    "content='" + string() + '\'' +
                    '}';
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}