package org.hvdw.fyt_sc9853i_extender.controllers;

import org.hvdw.fyt_sc9853i_extender.ProgramTexts;
import org.hvdw.fyt_sc9853i_extender.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;


public class TabOther {

    private final static Logger logger = LoggerFactory.getLogger(Utils.class);


    public boolean CopyRingtone(String flashDrive, String ringtoneFile) {
        boolean copysuccess = true;

        File source = new File(ringtoneFile);
        Path p = Paths.get(ringtoneFile);
        String base_ringtoneFile = p.getFileName().toString();
        String tmpdest = flashDrive + File.separator + "lsec_updatesh" + File.separator + "blink_ring.mp3";
        File dest = new File(tmpdest);
        try {
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            copysuccess = false;
            logger.error("copying ringtone failed");
        }

        return copysuccess;
    }

    public boolean downloadHostsFile(String flashdrive, String webUrl) {
        boolean copysuccess = true;

        String downloadfile = flashdrive + File.separator + "lsec_updatesh" + File.separator + "hosts";
        try {
            Utils.downloadUsingNIO(webUrl, downloadfile);
        } catch (IOException e) {
            copysuccess = false;
            logger.error("downloading hosts file failed.");
            e.printStackTrace();
        }
        return copysuccess;
    }

    public String dis_enable_Window_apk(JPanel myComponent) {
        String createstatus = "";

        String[] options = {"Disable", "Enable", "Cancel"};
        logger.info("Disable or enable window apk");

        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 350, ResourceBundle.getBundle("Strings").getString("popup_window_apk_text"), ProgramTexts.endHTML), ResourceBundle.getBundle("Strings").getString("btnWindowapk"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        if (choice == 0) { //Disable
            createstatus = Utils.CreateStandardFlashDrive(myComponent, "disable_window_apk.sh");
        } else if (choice == 1){ //Enable
            createstatus = Utils.CreateStandardFlashDrive(myComponent, "enable_window_apk.sh");
        } // Don't care about cancel


        return createstatus;
    }
}
