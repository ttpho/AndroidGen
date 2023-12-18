import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.*;

public class BuildAssets {

    private static final String KOTLIN_FILE_NAME = "BuildAssetManager.kt";
    private static final String ASSETS_FOLDER_NOT_FOUND = "Assets folder not found";
    private static final String ASSETS_FOLDER_EMPTY = "Assets is empty";
    private static final String UNKNOWN_CMD = "Unknown cmd, try \njavac BuildAssets.java && java BuildAssets app/src/main/assets app/src/main/java/com/company/app/package_name";

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(UNKNOWN_CMD);
            return;
        }
        final String assetsFolderPath = args[0];
        final String kotlinPath = args[1];

        final File folderAssets = getAssetFolderPathRoot(assetsFolderPath);
        if (folderAssets == null)
            return;

        final String importPackage = getImportPackage(getPackageName(kotlinPath));
        final String folderAssetsPath = folderAssets.getAbsolutePath();
        final List<String> listFilePath = allFileInAssets(folderAssets);

        if (listFilePath.isEmpty()) {
            System.out.println(ASSETS_FOLDER_EMPTY);
            return;
        }
        Collections.sort(listFilePath);
        List<String> lineKotlin = new ArrayList<>();
        for (final String filePath : listFilePath) {

            final String shortPath = filePath.replace(folderAssetsPath, "");
            final String uriPath = "file:///android_asset" + shortPath;
            final String constName = constantName(shortPath);
            final String kotlinLineCode = String.format("""
                        const val %s = "%s"
                    """, constName, uriPath);

            lineKotlin.add(kotlinLineCode);

        }

        final String contentFile = buildFileKotlinContent(importPackage, lineKotlin);

        final String fileFullPath = kotlinPath + "/" + KOTLIN_FILE_NAME;
        final Path path = Paths.get(fileFullPath);
        try {
            final File directory = new File(kotlinPath);
            if (!directory.exists()) {
                directory.mkdir();
            }
            writeFileWithString(path, contentFile);
            System.out.println("DONE!");
        } catch (Exception e) {
            System.err.format("Exception write file Kotlin: %s%n", e);
        }
    }

    private static String getPackageName(final String kotlinPath) {
        if (kotlinPath.contains("java/")) {
            return kotlinPath.split("java/")[1].replace("/", ".");
        }
        return "";
    }

    private static String getImportPackage(final String packageName) {
        if (packageName.isEmpty()) {
            return "";
        }

        return String.format("package %s", packageName);
    }

    private static String buildFileKotlinContent(final String importPackage, final List<String> lineCode) {
        StringBuilder lineCodeKotlin = new StringBuilder();

        for (final String line : lineCode) {
            lineCodeKotlin.append(line);
        }

        return String.format("""
                // GENERATED CODE - DO NOT MODIFY BY HAND
                %s

                object BuildAssetManager {
                %s
                }
                """, importPackage, lineCodeKotlin);
    }

    private static boolean isIgnoreFile(File file) {
        return file.getName().equals(".DS_Store");
    }

    public static void writeFileWithString(Path path, String content) throws IOException {
        path.toFile().createNewFile();
        Files.write(path, content.getBytes());
    }

    public static void search(final File folder, List<String> result) {
        for (final File f : Objects.requireNonNull(folder.listFiles())) {
            if (f.isDirectory()) {
                search(f, result);
            }
            if (f.isFile() && !isIgnoreFile(f)) {
                result.add(f.getAbsolutePath());
            }
        }
    }

    public static File getAssetFolderPathRoot(String assetFolderPath) {
        final File folderAssets = new File(assetFolderPath);
        if (!folderAssets.exists()) {
            System.out.println(ASSETS_FOLDER_NOT_FOUND);
            return null;
        }
        return folderAssets;
    }

    private static List<String> allFileInAssets(File folderAssets) {
        final List<String> allFileInAssets = new ArrayList<>();
        search(folderAssets, allFileInAssets);
        return allFileInAssets;
    }

    private static String constantName(String path) {
        final String[] split = path.split("/");
        final StringBuilder name = new StringBuilder();

        for (int i = 0; i < split.length; i++) {
            String word = split[i].toUpperCase();
            if (!word.isEmpty()) {
                name.append(word);
                if (i + 1 != split.length) {
                    name.append("_");
                }
            }
        }

        return name.toString().split("\\.")[0].replace("-", "_");
    }
}
