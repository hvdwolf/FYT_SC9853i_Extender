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

public class TabBackupRestore {

    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    public boolean CopyImage(String flashDrive, String image_type, String image) {
        boolean copysuccess = true;
        String tmpdest = "";

        File source = new File(image);
        Path p = Paths.get(image);
        String base_ringtoneFile = p.getFileName().toString();
        if ("boot".equals(image_type)) {
            tmpdest = flashDrive + File.separator + "boot.img";
        } else if ("recovery".equals(image_type)) {
            tmpdest = flashDrive + File.separator + "recovery.img";
        }

        File dest = new File(tmpdest);
        try {
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            copysuccess = false;
            logger.error("copying " + image_type + " image failed");
        }

        return copysuccess;
    }
    
    public void WriteImage(JPanel myComponent, String image_type) {
        String succeeded = "";

        if ("boot".equals(image_type)) {
            succeeded = Utils.CreateStandardFlashDrive(myComponent, "write_boot.sh");
        } else if ("recovery".equals(image_type)) {
            succeeded = Utils.CreateStandardFlashDrive(myComponent, "write_recovery.sh");
        }

        if ((!"failed".equals(succeeded)) && (!"no_flashdrive".equals(succeeded))) {
            String flashdrive = succeeded;
            // First remove possible images on flashdrive

            // Before asking the user for the image, we explain first
            if ("boot".equals(image_type)) {
                JOptionPane.showMessageDialog(myComponent, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("Strings").getString("restore_boot_img_text"), ProgramTexts.endHTML), ResourceBundle.getBundle("Strings").getString("btn_restore_boot_img"), JOptionPane.INFORMATION_MESSAGE);
            } else if ("recovery".equals(image_type)) {
                JOptionPane.showMessageDialog(myComponent, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("Strings").getString("restore_recovery_img_text"), ProgramTexts.endHTML), ResourceBundle.getBundle("Strings").getString("btn_restore_recovery_img"), JOptionPane.INFORMATION_MESSAGE);
            }
            String writable_img = Utils.getImage(myComponent, image_type); // select the image



            if (!"".equals(writable_img)) {
                // We have the boot or recovery image and the flashdrive location. Let's try to create our flashable content
                boolean write_succeeded = CopyImage(flashdrive, image_type, writable_img);
                if (write_succeeded) {
                    logger.info(image_type + " image flash drive created");
                    JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("contentCopiedText"), ResourceBundle.getBundle("Strings").getString("contentCopiedTitle"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    logger.info(image_type + " image flash drive failed");
                    String dialogText = String.format(ResourceBundle.getBundle("Strings").getString("copy_image_failed_text"), image_type);
                    JOptionPane.showMessageDialog(myComponent, dialogText, ResourceBundle.getBundle("Strings").getString("copy_image_failed_title"), JOptionPane.WARNING_MESSAGE);
                }
            } else {
                logger.debug("No " + image_type + " image selected");
                String dialogText = String.format(ResourceBundle.getBundle("Strings").getString("no_image_specified"), image_type);
                JOptionPane.showMessageDialog(myComponent, dialogText, ResourceBundle.getBundle("Strings").getString("no_image"), JOptionPane.WARNING_MESSAGE);
            }
        }
        
    }

}
