package ge.odvali.navigation;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LineMarker implements LineMarkerProvider {
    private final Icon ICON = IconLoader.getIcon("/arr.svg", this.getClass());

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        try {
            //this is how to find library and dependecies of project. Would be nice if disable features
            //if zerocode dependency is not present in the module.
//            Module module = ModuleUtil.findModuleForPsiElement(element);
//            final List<String> libraryNames = new ArrayList<String>();
//            ModuleRootManager.getInstance(module).orderEntries().forEachLibrary(library -> {
//                libraryNames.add(library.getName());
//                return true;
//            });
            Pattern pattern1 = Pattern.compile("\"scenarioName\"\\s*:\\s*\".*\"\\s*");
            if (pattern1.matcher(element.getText()).matches()) {
                var scenarioFileAbsolutePath = element.getContainingFile().getVirtualFile().getCanonicalPath();
                return calculateSingleLineMarkerInfo(element, scenarioFileAbsolutePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<? extends PsiElement> elements, @NotNull Collection<? super LineMarkerInfo<?>> result) {
        LineMarkerProvider.super.collectSlowLineMarkers(elements, result);
    }

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