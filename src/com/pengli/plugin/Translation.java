package com.pengli.plugin;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import org.apache.http.util.TextUtils;

import java.awt.*;

/**
 * Created by pengli on 16/6/2.
 */
public class Translation extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        final Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        if (null == mEditor) {
            return;
        }
        SelectionModel model = mEditor.getSelectionModel();
        final String selectedText = model.getSelectedText();
        if (TextUtils.isEmpty(selectedText)) {
            return;
        }
        String url = "http://fanyi.youdao.com/openapi.do?keyfrom=pengli&key=991847317&type=data&doctype=json&version=1.1&q="+selectedText;
        String result = HttpUtils.sendGet(url);
        Gson gson = new Gson();
        TranslationBean translation = gson.fromJson(result, TranslationBean.class);
        switch (translation.getErrorCode()){
            case 0:
                showPopWindow(mEditor, translation.toString());
                break;
            case 20:
                showPopWindow(mEditor,"要翻译的文本过长");
                break;
            case 30:
                showPopWindow(mEditor,"无法进行有效的翻译");
                break;
            case 40:
                showPopWindow(mEditor,"不支持的语言类型");
                break;
            case 50:
                showPopWindow(mEditor,"无效的key");
                break;
            case 60:
                showPopWindow(mEditor,"无词典结果，仅在获取词典结果生效");
                break;
            default:
                showPopWindow(mEditor,"请稍后再试");
                break;

        }

    }

    private void showPopWindow(final Editor mEditor,final String result) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                JBPopupFactory factory = JBPopupFactory.getInstance();
                factory.createHtmlTextBalloonBuilder(result, null, new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), null)
                        .setFadeoutTime(5000)
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(mEditor), Balloon.Position.below);
            }
        });
    }
}
