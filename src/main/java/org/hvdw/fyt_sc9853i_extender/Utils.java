package org.hvdw.fyt_sc9853i_extender;

import org.hvdw.fyt_sc9853i_extender.controllers.StandardFileIO;
import org.hvdw.fyt_sc9853i_extender.facades.SystemPropertyFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.hvdw.fyt_sc9853i_extender.facades.SystemPropertyFacade.SystemPropertyKey.*;

public class Utils {

    //private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);


    /*
     * Opens the default browser of the Operating System
     * and displays the specified URL
     */
    static void openBrowser(String webUrl) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(webUrl));
                return;
            }
            Application.OS_NAMES os = Utils.getCurrentOsName();

            // Such calls are extremely difficult to mock and test. In a future release, we should think about dependency injection and such stuff ;-)
            Runtime runtime = Runtime.getRuntime();
            switch (os) {
                case APPLE:
                    runtime.exec("open " + webUrl);
                    return;
                case LINUX:
                    runtime.exec("xdg-open " + webUrl);
                    return;
                case MICROSOFT:
                    runtime.exec("explorer " + webUrl);

            }
        }
        catch (IOException | IllegalArgumentException e) {
            //logger.error("Could not open browser", e);
        }
    }

    public static void downloadUsingNIO(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        File hosts = new File(file);
        if (hosts.exists()){
            hosts.delete();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

    // Displays the license in an option pane
    static void showLicense(JPanel myComponent) {

        String license = StandardFileIO.readTextFileAsStringFromResource("COPYING");
        JTextArea textArea = new JTextArea(license);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        JOptionPane.showMessageDialog(myComponent, scrollPane, "GNU GENERAL PUBLIC LICENSE Version 3", JOptionPane.INFORMATION_MESSAGE);
    }

    // Shows or hides the progressbar when called from some (long) running method
    static void progressStatus(JProgressBar progressBar, Boolean show) {
        if (show) {
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
            progressBar.setBorderPainted(true);
            progressBar.repaint();
        } else {
            progressBar.setVisible(false);
        }
    }

    /*
     * This method checks for a new version on the repo.
     * It can be called from startup (preferences setting) or from the Help menu
     */
/*    static void checkForNewVersion(String fromWhere) {
        String version = "";
        //boolean versioncheck = prefs.getByKey(VERSION_CHECK, false);


        if (fromWhere.equals("menu") || versioncheck) {
            try {
                URL url = new URL("https://raw.githubusercontent.com/hvdwolf/jExifToolGUI/master/version.txt");

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                version = in.readLine();
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            logger.info("Version: " + version);
            if (Float.valueOf(version) > Float.valueOf(ProgramTexts.Version)) {
                String[] options = {"No", "Yes"};
                int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 400, ProgramTexts.newVersionText), "New version found",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (choice == 1) { //Yes
                    // Do something
                    openBrowser("https://github.com/hvdwolf/jExifToolGUI/releases");
                    System.exit(0);
                }

            } else {
                if (fromWhere.equals("menu")) {
                    JOptionPane.showMessageDialog(null, String.format(ProgramTexts.HTML, 250, ProgramTexts.LatestVersionText), "No newer version", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    } */
    //////////////////////////////////////  unzip methods  //////////////////////////////////////////////////////////////
    // Copied from https://www.codejava.net/java-se/file-io/programmatically-extract-a-zip-file-using-java
    // use as Utils.unzip(zipFilePath, destDirectory);

    /* Size of the buffer to read/write data */
    private static final int BUFFER_SIZE = 4096;
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public static String unzip(String zipFilePath, String destDirectory) throws IOException {
        String unzipresult = "";

        logger.info("zipFilePath: " + zipFilePath + " ; destDirectory: " +destDirectory);
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            logger.debug("zip entry: " + filePath);
            unzipresult += filePath;
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        return unzipresult;
    }
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
    //////////////////////////////////////  end of unzip methods  //////////////////////////////////////////////////////////////

    //////////////////////////////////////  File choosers  //////////////////////////////////////////////////////////////
    public static String getFlashDrive(JPanel myComponent) {
        String SelectedDriveFolder;

        // Give user info popup to tell what's going to happen
        JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("askForFlashdrivedialogtext"), ResourceBundle.getBundle("Strings").getString("askForFlashdrivedialogtitle"), JOptionPane.INFORMATION_MESSAGE);

        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(ResourceBundle.getBundle("Strings").getString("locate_flashdrive"));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int status = chooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            SelectedDriveFolder = chooser.getSelectedFile().getAbsolutePath();
            return SelectedDriveFolder;
        } else {
            return "";
        }
    }

    public static String getRingtoneFile(JPanel myComponent) {
        String selectedringtoneFile;

        final JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        String[] filexts = {"mp3"};
        FileFilter filter = new FileNameExtensionFilter("(*.mp3)", filexts);
        chooser.setFileFilter(filter);
        chooser.setDialogTitle(ResourceBundle.getBundle("Strings").getString("locate_ringtone"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int status = chooser.showOpenDialog(myComponent);
        if (status == JFileChooser.APPROVE_OPTION) {
            selectedringtoneFile = chooser.getSelectedFile().getPath();
            return selectedringtoneFile;
        } else {
            return "";
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////  Standard flash drive content  //////////////////////////////////////////////////////////////
    //// functions below will create the standard content on the flash drive without additional content, like
    // lsec6521update
    // lsec_updatesh/lsec.sh

    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public static boolean eraseFlashdrive( String flashdrive) {
        boolean successfully_erased = true;

        logger.info("eraseFlashdrive: " + flashdrive);
        // First erase possible lsec6521update and Allapp.pkg in the root
        File lsec6521update = new File(flashdrive + File.separator + "lsec6521update");
        if (lsec6521update.exists()){
            lsec6521update.delete();
        }
        File Allapp_pkg = new File(flashdrive + File.separator + "Allapp.pkg");
        if (Allapp_pkg.exists()){
            Allapp_pkg.delete();
        }
        // Now erase the folder lsec_updatesh with contents
        File lsecsh = new File(flashdrive + File.separator + "lsec_updatesh" + File.separator + "lsec.sh");
        File lsec_updatesh = new File(flashdrive + File.separator + "lsec_updatesh");
        if ((lsecsh.exists()) || (lsec_updatesh.exists())) {
            boolean successfully_deleted = deleteDirectory(lsec_updatesh);
            if (!successfully_deleted) {
                successfully_erased = false;
                logger.error("Failed to erase folder structure");
            }
        }
        return successfully_erased;
    }
    
    public static String extract_resource_to_flash(String resourcePath, String flashdrive){
        String copyresult = "";
        Path resourceFile = Paths.get(flashdrive + File.separator);

        try {
            InputStream fileStream =StandardFileIO.getResourceAsStream(resourcePath);
            if(fileStream == null)
                return null;

            // Don't use this one eraseFlashdrive here as it will do it again on every extracted resource file thereby removing previously extracted files
            //eraseFlashdrive(FlashDrive);
            // Grab the file name
            String[] chopped = resourcePath.split("\\/");
            String fileName = chopped[chopped.length-1];
            // First check if we have an Allapp.pkg
            File Allapp_pkg = new File(flashdrive + File.separator + "Allapp.pkg");
            if (Allapp_pkg.exists()){
                Allapp_pkg.delete();
            }
            // Then delete and create our file or folder/file
            if (fileName == "lsec6521update") {
                File lsec6521update = new File(flashdrive + File.separator + "lsec6521update");
                if (lsec6521update.exists()){
                    lsec6521update.delete();
                }
                resourceFile = Paths.get(flashdrive + File.separator + "lsec6521update");
            } else{
                File lsecsh = new File(flashdrive + File.separator + "lsec_updatesh" + File.separator + "lsec.sh");
                File lsec_updatesh = new File(flashdrive + File.separator + "lsec_updatesh");
                if ((lsecsh.exists()) || (lsec_updatesh.exists())) {
                    boolean successfully_deleted = deleteDirectory(lsec_updatesh);
                    if (!successfully_deleted) {
                        copyresult = "Failed to erase folder structure";
                        logger.error("Failed to erase folder structure");
                    }
                }
                resourceFile = Paths.get(flashdrive + File.separator + "lsec_updatesh" + File.separator + "lsec.sh");
            }
            // First create lsec_updatesh folder on flash drive
            try {
                Files.createDirectories(Paths.get(flashdrive + File.separator + "lsec_updatesh"));
            } catch (IOException ioe) {
                ioe.printStackTrace();
                copyresult = "Creating directory lsec_updatesh failed";
                logger.error("Creating directory lsec_updatesh failed");
            }

            // Create an output stream
            OutputStream out = new FileOutputStream(String.valueOf(resourceFile));

            // Write the file
            byte[] buffer = new byte[1024];
            int len = fileStream.read(buffer);
            while (len != -1) {
                out.write(buffer, 0, len);
                len = fileStream.read(buffer);
            }

            // Close the streams
            fileStream.close();
            out.close();

        } catch (IOException e) {
            copyresult = "Error creating content flash drive.";
            logger.error("Error creating content flash drive.");
            return null;
        }
        if ("".equals(copyresult)) {
            copyresult = "success";
            logger.info("success");
        }
        return copyresult;
    }

    public static String CreateStandardFlashDrive (JPanel myComponent, String Script){
        // Give user info popup to tell what's going to happen
        //JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("askForFlashdrivedialogtext"), ResourceBundle.getBundle("Strings").getString("askForFlashdrivedialogtitle"), JOptionPane.INFORMATION_MESSAGE);
        // Ask for flash drive
        String FlashDrive = Utils.getFlashDrive(myComponent);
        if (!"".equals(FlashDrive)) {
            String lsec6521updatecopied = Utils.extract_resource_to_flash("lsec6521update", FlashDrive);
            String scriptcopied = Utils.extract_resource_to_flash("scripts/"+Script, FlashDrive);
            if (("success".equals(lsec6521updatecopied)) && ("success").equals(scriptcopied)) {
                logger.info(FlashDrive);
                return FlashDrive;
            } else {
                JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("flashdrive_failed_text"), ResourceBundle.getBundle("Strings").getString("flashdrive_failed_title"), JOptionPane.WARNING_MESSAGE);
                logger.debug(ResourceBundle.getBundle("Strings").getString("flashdrive_failed_title"));
                return "failed";
            }
        } else {
            JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("no_flashdrive_specified"), ResourceBundle.getBundle("Strings").getString("no_flashdrive"), JOptionPane.WARNING_MESSAGE);
            logger.debug(ResourceBundle.getBundle("Strings").getString("no_flashdrive"));
            return "no_flashdrive";
        }

    }

    //////////////////////////////////////  End of Standard flash drive content  //////////////////////////////////////////////////////////////

    public static Application.OS_NAMES getCurrentOsName() {
        String OS = SystemPropertyFacade.getPropertyByKey(OS_NAME).toLowerCase();
        if (OS.contains("mac")) return Application.OS_NAMES.APPLE;
        if (OS.contains("windows")) return Application.OS_NAMES.MICROSOFT;
        return Application.OS_NAMES.LINUX;
    }

    public static boolean isOsFromMicrosoft() {
        return getCurrentOsName() == Application.OS_NAMES.MICROSOFT;
    }


    @SuppressWarnings("SameParameterValue")
    private static URL getResource(String path) {
        return Utils.class.getClassLoader().getResource(path);
    }
}
