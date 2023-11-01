package org.wuerthner.sport.util;

import org.wuerthner.sport.api.Executor;

import java.io.File;

public class PdfViewerExecutor implements Executor<File> {
    public void run(File pdfFile) {
        try {
            if (isLinux()) {
                Runtime.getRuntime().exec("evince " + pdfFile.getCanonicalPath(), null, pdfFile.getParentFile());
            } else if (isWindows()) {
                Runtime.getRuntime().exec("C:\\Program Files (x86)\\Adobe\\Acrobat Reader DC\\Reader\\AcroRd32.exe " + pdfFile.getCanonicalPath(), null,
                        pdfFile.getParentFile());

                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + pdfFile.getCanonicalPath(), null, pdfFile.getParentFile());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    private static boolean isLinux() {
        return System.getProperty("os.name").startsWith("Linux");
    }
}
