package com.example.chatsystem.model;

import java.io.Serializable;

/*

逻辑时钟是在每个节点维护一个单调递增的数字，称之为时间戳，每一个操作都会将这个时间戳增加一，同时将这个数字返回给调用方。
调用方调用服务器操作时候，也回带过来最新的时间戳，服务器选取请求中的与本地的时间戳中较大的一个进行加一操作，
然后设置为本地最新的时间戳。发起调用的调用方在完成每次调用后，也会选择本地与调用返回中较大者，进行加一操作，
之后设置为本地最新的时间戳。同一个节点内部的时间戳，或者从同一个节点返回的时间戳可以用于比较时间的先后顺序。
不同节点的时间戳不能用于比较先后顺序。所以这个集合也称为偏序关系，而不是全序。
在这些时间戳的集合中，并不是任意两个元素（时间戳）都可以进行比较。

 */
public class Message implements Serializable {

       private String id;
    private long timestamp;
    private String content;
    private String peerId;

    public Message() {
    }
    public Message(String id, long timestamp, String content,String peerId) {
        this.id = id;
        this.timestamp = timestamp;
        this.content = content;
        this.peerId=peerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPeerId() {
        return peerId;
    }

    public void setPeerId(String peerId) {
        this.peerId = peerId;
    }
}