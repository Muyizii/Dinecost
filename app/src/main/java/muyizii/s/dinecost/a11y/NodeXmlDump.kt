package muyizii.s.dinecost.a11y

import android.util.Xml
import android.view.accessibility.AccessibilityNodeInfo
import org.xmlpull.v1.XmlSerializer
import java.io.StringWriter

fun nodeToXmlString(rootNode: AccessibilityNodeInfo): String {
    val serializer = Xml.newSerializer()
    val writer = StringWriter()

    serializer.setOutput(writer)
    serializer.startDocument("UTF-8", true)
    serializer.startTag("", "hierarchy")
    serializer.attribute("", "rotation", "0")

    // 递归生成节点 XML
    dumpNodeToXml(serializer, rootNode)

    serializer.endTag("", "hierarchy")
    serializer.endDocument()

    return writer.toString()
}

private fun dumpNodeToXml(
    serializer: XmlSerializer,
    node: AccessibilityNodeInfo,
    index: Int = 0
) {
    serializer.startTag("", "node")

    // 添加节点属性
    serializer.attribute("", "index", index.toString())
    serializer.attribute("", "text", node.text?.toString() ?: "")
    serializer.attribute("", "resource-id", node.viewIdResourceName ?: "")
    serializer.attribute("", "class", node.className?.toString() ?: "")
    serializer.attribute("", "package", node.packageName?.toString() ?: "")
    serializer.attribute("", "content-desc", node.contentDescription?.toString() ?: "")

    // 递归处理子节点
    for (i in 0 until node.childCount) {
        val child = node.getChild(i)
        if (child != null) {
            dumpNodeToXml(serializer, child, i)
            child.recycle() // 及时释放资源
        }
    }

    serializer.endTag("", "node")
}