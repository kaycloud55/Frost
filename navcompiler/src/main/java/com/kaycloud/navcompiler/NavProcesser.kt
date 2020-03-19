package com.kaycloud.navcompiler

import com.google.auto.service.AutoService
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kaycloud.navannotation.ActivityDestination
import com.kaycloud.navannotation.FragmentDestination
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation
import kotlin.math.abs

/**
 * author: jiangyunkai
 * Created_at: 2020-02-19
 */

const val OUTPUT_FILE_NAME = "destination.json"

/**
 * APP页面导航信息收集注解处理器
 * <p>
 * AutoService注解：就这么一标记，annotationProcessor  project()应用一下,编译时就能自动执行该类了。
 * <p>
 * SupportedSourceVersion注解:声明我们所支持的jdk版本
 * <p>
 * SupportedAnnotationTypes:声明该注解处理器想要处理那些注解
 */

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(
    "com.kaycloud.navannotation.ActivityDestination",
    "com.kaycloud.navannotation.FragmentDestination"
)
class NavProcesser : AbstractProcessor() {

    private var messager: Messager? = null
    private var filer: Filer? = null


    override fun init(processingEnvironment: ProcessingEnvironment?) {
        super.init(processingEnvironment)
        //日志打印，非Android模块，不能使用android.util.log.e()
        messager = processingEnvironment?.messager
        //文件处理工具
        filer = processingEnvironment?.filer
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        //通过处理器环境上下文roundEnv分别获取 项目中标记的FragmentDestination.class 和ActivityDestination.class注解。
        //此目的就是为了收集项目中哪些类 被注解标记了
        val fragmentElements = roundEnv?.getElementsAnnotatedWith(FragmentDestination::class.java)
        val activityElements = roundEnv?.getElementsAnnotatedWith(ActivityDestination::class.java)

        if (!fragmentElements.isNullOrEmpty() || !activityElements.isNullOrEmpty()) {
            val destMap = hashMapOf<String, JsonObject>()
            //分别处理FragmentDestination和ActivityDestination注解类型
            //并收集到destMap中，以及就能记录下所有的页面信息了
            handleDestination(fragmentElements, FragmentDestination::class.java, destMap)
            handleDestination(activityElements, ActivityDestination::class.java, destMap)

            //app/src/main/assets
            var fos: FileOutputStream? = null
            var writer: OutputStreamWriter? = null

            //filer.createResource()意思是创建源文件
            //我们可以指定为class文件输出的地方，
            //StandardLocation.CLASS_OUTPUT：java文件生成class文件的位置，/app/build/intermediates/javac/debug/classes/目录下
            //StandardLocation.SOURCE_OUTPUT：java文件的位置，一般在/ppjoke/app/build/generated/source/apt/目录下
            //StandardLocation.CLASS_PATH 和 StandardLocation.SOURCE_PATH用的不多，指的了这个参数，就要指定生成文件的pkg包名了
            val resource =
                filer?.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME)
            val resourcePath = resource?.toUri()?.path
            messager?.printMessage(Diagnostic.Kind.NOTE, "resourcePath:$resourcePath")

            //由于我们想要把json文件生成在app/src/main/assets/目录下,所以这里可以对字符串做一个截取，
            //以此便能准确获取项目在每个电脑上的 /app/src/main/assets/的路径

            val appPath = resourcePath?.substring(0, resourcePath?.indexOf("app")?.plus(4))
            val assetsPath = appPath + "src/main/assets"

            val file = File(assetsPath)
            if (!file.exists()) {
                file.mkdirs()
            }

            //此处就是稳健的写入了
            val outputFile = File(file, OUTPUT_FILE_NAME)
            if (outputFile.exists()) {
                outputFile.delete()
            }
            outputFile.createNewFile()
            //利用gson把收集到的页面信息转换成json格式的，并输出到文件中
            val content = Gson().toJson(destMap)
            fos = FileOutputStream(outputFile)
            fos.use {
                writer = OutputStreamWriter(fos, "utf-8")
                writer?.use {
                    it.write(content)
                    it.flush()
                }
            }
        }
        return true
    }

    private fun handleDestination(
        elements: Set<Element>?,
        annotationClass: Class<out Annotation>?,
        destMap: MutableMap<String, JsonObject>?
    ) {
        elements?.forEach { element ->
            //TypeElement是Element的一种。
            //如果我们的注解标记在了类名上。所以可以直接强转一下。使用它得到全类名
            val typeElement = element as TypeElement
            //全类名，包含包名
            val className = typeElement.qualifiedName.toString()
            //页面的id，此处不能重复，使用页面的类名做hashCode即可
            val id = abs(className.hashCode())
            //页面的pageUrl相当于隐式跳转意图中的host://scheme//path格式
            var pageUrl = ""
            //是否需要登录
            var needLogin = false
            //是否作为首页的第一个展示的页面
            var asStarter = false
            //标记页面的类型
            var isFragment = false

            val annotation = element.getAnnotation(annotationClass)
            if (annotation is FragmentDestination) {
                pageUrl = annotation.pageUrl
                asStarter = annotation.asStarter
                needLogin = annotation.needLogin
                isFragment = true
            } else if (annotation is ActivityDestination) {
                pageUrl = annotation.pageUrl
                asStarter = annotation.asStarter
                needLogin = annotation.needLogin
                isFragment = false
            }

            if (destMap?.containsKey(pageUrl) == true) {
                messager?.printMessage(Diagnostic.Kind.ERROR, "不同的页面不允许使用相同的pageUrl:$className")
            } else {
                val jsonObject = JsonObject()
                jsonObject.addProperty("id", id)
                jsonObject.addProperty("needLogin", needLogin)
                jsonObject.addProperty("asStarter", asStarter)
                jsonObject.addProperty("pageUrl", pageUrl)
                jsonObject.addProperty("className", className)
                jsonObject.addProperty("isFragment", isFragment)
                destMap?.put(pageUrl, jsonObject)

            }
        }
    }

}