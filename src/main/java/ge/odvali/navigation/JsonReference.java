package ge.odvali.navigation;

import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonReference extends PsiPolyVariantReferenceBase<PsiElement> {
    public JsonReference(PsiElement psiElement) {
        super(psiElement, true);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        try {
            String elementText = getElement().getText();
            String fileNameToSearch = "";
            if (elementText.contains(":")) {
                fileNameToSearch = elementText.substring(elementText.indexOf("/") + 1, elementText.lastIndexOf("}"));
                var files = FilenameIndex.getFilesByName(getElement().getProject(), fileNameToSearch,
                        GlobalSearchScope.projectScope(getElement().getProject()));
                return PsiElementResolveResult.createResults(filterFoundFilesByPath(files,
                        elementText.substring(elementText.indexOf(":") + 1, elementText.indexOf("}")),
                        false));
            } else {
                fileNameToSearch = elementText.substring(elementText.lastIndexOf('.') + 1).replace("\"", "") + ".java";
                var files = FilenameIndex.getFilesByName(getElement().getProject(), fileNameToSearch,
                        GlobalSearchScope.projectScope(getElement().getProject()));
                return PsiElementResolveResult.createResults(filterFoundFilesByPath(files, elementText, true));
            }

        } catch (Exception e) {
            if (e instanceof ProcessCanceledException) {
                throw e;
            } else {
                e.printStackTrace();
            }
        }
        return PsiElementResolveResult.createResults(List.of());
    }

    private List<PsiFile> filterFoundFilesByPath(PsiFile[] files, String path, boolean isJavaClass) {
        if (isJavaClass) {
            return Arrays.stream(files)
                    .filter(file -> file.getVirtualFile().getCanonicalPath().replace("/", ".").contains(path.replace("\"", "") +
                            ".java"))
                    .collect(Collectors.toList());
        }
        return Arrays.stream(files)
                .filter(file -> file.getVirtualFile().getCanonicalPath().contains(path.replace("\"", "")))
                .collect(Collectors.toList());

    }
}
