我们通过这章几个问题的调试，可以总结一下http response的内容。
为了彻底搞清楚http response的内容，我们希望能够看到http response raw stream
但是，无论是通过httpclient，还是主流的浏览器，我们发现都没法看到http response raw stream
我们看到的都是已经经过解析的内容。

怎么办呢？
首先，我们可以类比一下，http reqeust的处理过程。
我们在之前的章节中，从源码层级，实现过对http request的处理。
这没啥花头的，就是逐行处理就行了
那么对于http response，我们大胆假设，也是这样：
第一块：statusline: HTTP/1.1 200 OK
第二块：content
第三块：http header

为了证实我们的猜想，我们看一下返回http response内容的源码：

1.HttpProcessor.java
HttpProcessor.process()
我们调用SimpleContainer处理完servlet之后，调用以下代码：
response.finishResponse();

2.HttpResponseBase.java
HttpResponseBase.finishResponse()

我们在finishResponse()中可以看到:
sendHeaders();
super.finishResponse();

首先当然是发送http headers
然后调用父类关闭流

sendHeaders()方法展开来讲一下
其实顺序就是先打印statusline，再打印http header

但是我们要关注一下sendHeaders()方法中，获取OutputStreamWriter的方式：
OutputStreamWriter osr = new OutputStreamWriter(getStream(), getCharacterEncoding());
可以看到，是直接拿client socket outputstream创建的OutputStreamWriter类

3.Server中生成response content的代码
反观我们在Servlet中，输出servlet结果的PrintWriter类的生成方式：
PrintWriter writer = response.getWriter();
writer.println("<html>");
......

这个getWriter()方法请参考：
ResponseBase.getWriter()

ResponseStream newStream = (ResponseStream) createOutputStream();
newStream.setCommit(false);
OutputStreamWriter osr =
            new OutputStreamWriter(newStream, getCharacterEncoding());
writer = new ResponseWriter(osr, newStream);
stream = newStream;
return (writer);

其实是差不多的，就是封装了一层ResponseWriter()类而已


综合以上，我们可以看到，输出顺序貌似是：
content->status line->http header
为了证实我们的猜想，建议看一下http client的源码，看http client是怎么解析server response的




