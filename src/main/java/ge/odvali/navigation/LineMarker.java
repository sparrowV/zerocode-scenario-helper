package ge.odvali.navigation;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LineMarker implements LineMarkerProvider {
    private final Icon ICON = IconLoader.getIcon("/arrow.png", this.getClass());

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        Pattern pattern = Pattern.compile("\"scenarioName\":\\s*\".*\"\\s*");
        if (pattern.matcher(element.getText()).matches()) {
            var scenarioFileAbsolutePath = element.getContainingFile().getVirtualFile().getCanonicalPath();
            return calculateSingleLineMarkerInfo(element, scenarioFileAbsolutePath);
        }
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<? extends PsiElement> elements, @NotNull Collection<? super LineMarkerInfo<?>> result) {
        LineMarkerProvider.super.collectSlowLineMarkers(elements, result);
    }

    @Nullable
    private LineMarkerInfo calculateSingleLineMarkerInfo(@NotNull PsiElement psiElement, String scenarioFileAbsolutePath) {
        Collection<VirtualFile> allJavaFiles = FilenameIndex.getAllFilesByExt(psiElement.getProject(),
                "java",
                GlobalSearchScope.projectScope(psiElement.getProject()));
        List<VirtualFile> targetFiles = new ArrayList<>();
        var name = scenarioFileAbsolutePath.substring(scenarioFileAbsolutePath.lastIndexOf("/test/resources") + 1).replace("test" +
                "/resources/", "");
        allJavaFiles.forEach(javaFile -> {
            CharSequence charSequence = LoadTextUtil.loadText(javaFile);
            if (charSequence.toString().contains(name)) {
                targetFiles.add(javaFile);

            }
        });
        var psiManager = PsiManager.getInstance(psiElement.getProject());
        return NavigationGutterIconBuilder
                .create(ICON)
                .setTargets(targetFiles.stream()
                        .map(psiManager::findFile)
                        .collect(Collectors.toList()))
                .setTooltipText("Navigate to Test Class")
                .createLineMarkerInfo(psiElement);
    }
}