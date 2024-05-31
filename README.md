项目设计报告：P2P群聊系统

 1. 介绍

本项目旨在设计和实现一个点对点（P2P）群聊系统，其中包含多个Peer节点。每个节点都保存完整的群聊过程记录，并提供客户端软件，允许用户发送聊天信息，并实时显示完整的群聊过程。

 2. 系统架构

 2.1 节点拓扑

本系统采用点对点拓扑，每个节点都可以直接与其他节点通信，形成一个分布式网络。节点之间的通信通过Socket实现。

 2.2 软件组件

- 客户端软件：每个节点都有一个客户端页面，用于用户输入聊天信息并将其发送到网络上。客户端软件还负责显示完整的群聊过程。
  
- 服务器端：每个节点都有一个服务器端，用于处理来自其他节点的消息，并将其分发给本地的客户端软件。

 2.3 数据格式

每条聊天消息采用以下格式：

```plaintext
<message_id, timestamp, message_content,peer_id>
```

- `message_id`：消息的全局唯一ID号，用于标识每条消息。
- `timestamp`：消息的时间戳，用于确保因果关系。
- `message_content`：消息的文本内容。
-’peer_id’:发送消息的节点id号

节点格式：
private String peerId;
private String peerName;
private String ip;
private int port;

 3. 功能实现

 3.1 客户端功能

- 发送消息：允许用户输入消息，并将其发送到网络上。
- 显示群聊过程：实时显示完整的群聊过程，确保因果关系。
- 用户界面：提供友好的用户界面，方便用户交互。

    public void actionPerformed(ActionEvent e)
    {
        // 如果事件源是登录按钮
        if(e.getSource()==loginButton)
        {
            // 调用登录方法
            Login();
        }
        // 如果事件源是注销按钮
        else if(e.getSource()==logoutButton)
        {
            // 调用注销方法
            Logout();
            // 退出程序
            System.exit(0);
        }
        // 如果事件源是发送按钮
        else if(e.getSource()==sendButton)
        {
            // 如果输入框为空或者为null
            if(taInput.getText().equalsIgnoreCase("")||taInput.getText()==null)
            {
                // 显示提示对话框，提示用户尚未输入聊天信息
                JOptionPane.showMessageDialog(this, "您尚未输入聊天信息!","Information" , JOptionPane.INFORMATION_MESSAGE);
            }
            // 调用发送消息方法
            sendMessage();
        }
        // 如果事件源是清空按钮
        else if(e.getSource()==cleanButton)
        {
            // 清空输入框的内容
            taInput.setText("");
        }
    }
 


 3.2 服务器端功能

- 接收消息：接收来自其他节点的消息。
- 分发消息：将接收到的消息分发给本地的客户端软件。
- 保存聊天记录：将收到的消息保存到本地的群聊记录中。

    @Override
    public void actionPerformed(ActionEvent e) {
        // 如果事件源是启动按钮
        if(e.getSource()==startButton)
        {
            // 调用启动服务器的方法
            startServer();
        }
        // 如果事件源是关闭按钮
        else if(e.getSource()==closeButton)
        {
            // 调用停止服务器的方法
            stopServer();
            // 退出程序
            System.exit(0);
        }
        // 如果事件源是发送按钮
        else if(e.getSource()==sendButton)
        {
            // 如果输入框的内容为空或null
            if(taInput.getText().equalsIgnoreCase("")||taInput.getText()==null)
            {
                // 弹出提示框，提示尚未输入系统信息
                JOptionPane.showMessageDialog(this, "尚未输入系统信息!","Information" , JOptionPane.INFORMATION_MESSAGE);
            }
            // 调用发送系统消息的方法
            sendSystemMessage();
        }
        // 如果事件源是清空按钮
        else if(e.getSource()==cleanButton)
        {
            // 清空输入框的内容
            taInput.setText("");
        }
    }

 4. 技术选型

- 编程语言：Java.
- 网络通信：使用Socket进行节点间的通信。
- 界面设计：用Jframe实现客户端软件的用户界面。

 5. 实现步骤

1. 设计并实现客户端软件，包括用户界面和消息发送功能。
2. 设计并实现服务器端，处理消息的接收和分发功能。
3. 实现消息的传输和处理逻辑，确保因果关系。
4. 测试系统功能，并进行调试和优化。
5. 编写文档，包括用户手册和技术文档。

 6. 风险和挑战

- 网络通信稳定性：需要处理网络延迟、丢包等问题，确保消息能够可靠地传输。
- 并发处理：需要处理多个节点同时发送消息的情况，确保系统能够正确地处理并发。
- 用户体验：需要设计用户友好的界面，并确保系统稳定性和响应速度。

 7. 总结

本项目设计了一个P2P群聊系统，实现了节点间的实时通信和群聊功能。通过合理的架构设计和技术选型，能够满足用户的需求，并确保系统的稳定性和可靠性。

