package com.libin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.awt.font.TextAttribute.UNDERLINE;

public class MvpFrameAction extends AnAction {

    private Project project;

    private String mPackageName = "";   //包名
    private String mAuthor;    //作者
    private String mModelName;    //创建文件的前缀
    private String mDescribe;    //应用描述
    private boolean isActivity;

    private enum CodeType {
        Activity, ActivityContract, ActivityPresenter, ActivityModel,
        Fragment, FragmentContract, FragmentPresenter, FragmentModel,
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getData(PlatformDataKeys.PROJECT);
        mPackageName = getPackageName();
        init();
        refreshProject(e);
    }

    /**
     * 刷新项目
     *
     * @param e
     */
    private void refreshProject(AnActionEvent e) {
        e.getProject().getBaseDir().refresh(false, true);
    }

    /**
     * 初始化Dialog
     */
    private void init() {
        MyDialog myDialog = new MyDialog(new MyDialog.DialogCallBack() {
            @Override
            public void ok(String author, String modleName, String describe, boolean isAc) {
                if (author == null || modleName == null || modleName.length() == 0 || author.length() == 0) {
                    Messages.showInfoMessage(project, "something is empty", "Error");
                    return;
                }
                isActivity = isAc;
                mAuthor = author;
                mModelName = modleName;
                mDescribe = describe;
                System.out.println("==mModelName=="+mModelName);
                if (isActivity) {
                    createActivityClassFiles();
                } else {
                    createFragmentClassFiles();
                }
                Messages.showInfoMessage(project, "create mvp code success", "Success");
            }
        });
        myDialog.setVisible(true);

    }

    /**
     * 生成类文件
     */
    private void createClassFiles() {
        createClassFile(CodeType.Activity);
        createClassFile(CodeType.Fragment);
    }

    private void createActivityClassFiles() {
        createClassFile(CodeType.Activity);
        createClassFile(CodeType.ActivityContract);
        createClassFile(CodeType.ActivityPresenter);
        createClassFile(CodeType.ActivityModel);
    }

    private void createFragmentClassFiles() {
        createClassFile(CodeType.Fragment);
        createClassFile(CodeType.FragmentContract);
        createClassFile(CodeType.FragmentPresenter);
        createClassFile(CodeType.FragmentModel);
    }

    /**
     * 生成mvp框架代码
     *
     * @param codeType
     */
    private void createClassFile(CodeType codeType) {
        String fileName = "";
        String content = "";
        String appPath = getAppPath();

        String xmlName = "";
        String xmlContent = "";
        String xmlPath = getXmlPath();
        switch (codeType) {
            case Activity:
                fileName = "TemplateActivity.txt";
                content = ReadTemplateFile(fileName);
                content = dealTemplateContent(content);
                writeToFile(content, appPath + mModelName.toLowerCase(), upperCaseFirstLatter(mModelName) + "Activity.java");

                xmlName = "TemplateXml.txt";
                xmlContent = ReadTemplateFile(xmlName);
                writeToFile(xmlContent, xmlPath, "activity_" + mModelName.toLowerCase() + ".xml");

                System.out.println("====转换1==="+mModelName.toLowerCase());
                System.out.println("====转换2==="+upperCaseFirstLatter(mModelName));
                System.out.println("====转换2==="+lowerCaseFirstLatter(mModelName));
                break;
            case ActivityContract:
                fileName = "TemplateActivityContract.txt";
                content = ReadTemplateFile(fileName);
                content = dealTemplateContent(content);
                writeToFile(content, appPath + mModelName.toLowerCase(), upperCaseFirstLatter(mModelName) + "Contract.java");
                break;
            case ActivityModel:
                fileName = "TemplateActivityModel.txt";
                content = ReadTemplateFile(fileName);
                content = dealTemplateContent(content);
                writeToFile(content, appPath + mModelName.toLowerCase(), upperCaseFirstLatter(mModelName) + "Model.java");
                break;
            case ActivityPresenter:
                fileName = "TemplateActivityPresenter.txt";
                content = ReadTemplateFile(fileName);
                content = dealTemplateContent(content);
                writeToFile(content, appPath + mModelName.toLowerCase(), upperCaseFirstLatter(mModelName) + "Presenter.java");
                break;
            case Fragment:
                fileName = "TemplateFragment.txt";
                content = ReadTemplateFile(fileName);
                content = dealTemplateContent(content);
                writeToFile(content, appPath + mModelName.toLowerCase(), upperCaseFirstLatter(mModelName) + "Fragment.java");

                xmlName = "TemplateXml.txt";
                xmlContent = ReadTemplateFile(xmlName);
                writeToFile(xmlContent, xmlPath, "fragment_" + mModelName.toLowerCase() + ".xml");
                break;
            case FragmentContract:
                fileName = "TemplateFragmentContract.txt";
                content = ReadTemplateFile(fileName);
                content = dealTemplateContent(content);
                writeToFile(content, appPath + mModelName.toLowerCase(), upperCaseFirstLatter(mModelName) + "Contract.java");
                break;
            case FragmentModel:
                fileName = "TemplateFragmentModel.txt";
                content = ReadTemplateFile(fileName);
                content = dealTemplateContent(content);
                writeToFile(content, appPath + mModelName.toLowerCase(), upperCaseFirstLatter(mModelName) + "Model.java");
                break;
            case FragmentPresenter:
                fileName = "TemplateFragmentPresenter.txt";
                content = ReadTemplateFile(fileName);
                content = dealTemplateContent(content);
                writeToFile(content, appPath + mModelName.toLowerCase(), upperCaseFirstLatter(mModelName) + "Presenter.java");
                break;
        }
    }

    /**
     * 首字母大写
     * @param str
     * @return
     */
    public static String upperCaseFirstLatter(String str){
        if(Character.isUpperCase(str.charAt(0))){
            return str;
        }else {
            char[] strChar=str.toCharArray();
            strChar[0]-=32;
            return String.valueOf(strChar);
        }
    }

    public static String lowerCaseFirstLatter(String str){
        if(Character.isLowerCase(str.charAt(0))){
            return str;
        }else {
            char[] strChar=str.toCharArray();
            strChar[0]+=32;
            return String.valueOf(strChar);
        }
    }

    /**
     * 驼峰转下划线
     */
    public static String humpToLine(String param){
        param = lowerCaseFirstLatter(param);
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (Character.isUpperCase(c)){
                sb.append('_');
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 获取包名文件路径
     *
     * @return
     */
    private String getAppPath() {
        String packagePath = mPackageName.replace(".", "/");
        String appPath;
        appPath = project.getBasePath() + "/app/src/main/java/" + packagePath + "/mvp/";
        System.out.println("=====appPath== java ====" + appPath);

        return appPath;
    }

    /**
     * 获取包名文件路径
     *
     * @return
     */
    private String getXmlPath() {
        String appPath;
        appPath = project.getBasePath() + "/app/src/main/res/layout/";
        System.out.println("=====appPath=== layout ===" + appPath);
        return appPath;
    }

    /**
     * 替换模板中字符
     *
     * @param content
     * @return
     */
    private String dealTemplateContent(String content) {
        if (content.contains("$name")) {
            content = content.replace("$name", upperCaseFirstLatter(mModelName));
        }
        if (content.contains("$lName")) {
            content = content.replace("$lName", lowerCaseFirstLatter(mModelName));
        }
        if (content.contains("$xmlhumptoline")) {
            content = content.replace("$xmlhumptoline", humpToLine(upperCaseFirstLatter(mModelName)));
        }
        if (content.contains("$packagename")) {
            content = content.replace("$packagename", mPackageName + ".mvp." + mModelName.toLowerCase());
        }
        if (content.contains("$basepackagename")) {
            content = content.replace("$basepackagename", mPackageName);
        }
        content = content.replace("$author", mAuthor);
        content = content.replace("$description", mDescribe);
        content = content.replace("$date", getDate());
        return content;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public String getDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 读取模板文件中的字符内容
     *
     * @param fileName 模板文件名
     * @return
     */
    private String ReadTemplateFile(String fileName) {
        InputStream in = null;
        in = this.getClass().getResourceAsStream("/Template/" + fileName);
        String content = "";
        try {
            content = new String(readStream(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


    private byte[] readStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
            inputStream.close();
        }

        return outputStream.toByteArray();
    }


    /**
     * 生成
     *
     * @param content   类中的内容
     * @param classPath 类文件路径
     * @param className 类文件名称
     */
    private void writeToFile(String content, String classPath, String className) {
        try {
            File floder = new File(classPath);
            if (!floder.exists()) {
                floder.mkdirs();
            }

            File file = new File(classPath + "/" + className);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 从AndroidManifest.xml文件中获取当前app的包名
     *
     * @return
     */
    private String getPackageName() {
        String package_name = "packagename";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(project.getBasePath() + "/app/src/main/AndroidManifest.xml");

            NodeList nodeList = doc.getElementsByTagName("manifest");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element element = (Element) node;
                package_name = element.getAttribute("package");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return package_name;
    }
}