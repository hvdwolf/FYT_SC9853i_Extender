package org.hvdw.fyt_sc9853i_extender.controllers;

import org.hvdw.fyt_sc9853i_extender.MyConstants;
import org.hvdw.fyt_sc9853i_extender.ProgramTexts;
import org.hvdw.fyt_sc9853i_extender.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TabMods {

    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    public String download_Zip(String zip_path_file) {
        String downloadedzip = "";
        String tmpdownloadzip = zip_path_file;

        // Get the temporary directory
        String tempDir = System.getProperty("java.io.tmpdir");

        // Which mod?
        if (zip_path_file.contains("com_syu_ms")) {
            tmpdownloadzip = tempDir + File.separator + "com_syu_ms.zip";
        } else if (zip_path_file.contains("com_syu_radio")) {
            tmpdownloadzip = tempDir + File.separator + "com_syu_radio.zip";
        }

        String webUrl = zip_path_file;
        try {
            Utils.downloadUsingNIO(webUrl, tmpdownloadzip);
            downloadedzip = tmpdownloadzip;
            logger.info("downloading zip " + downloadedzip + " successful");
        } catch (IOException e) {
            downloadedzip = "zip download failed";
            logger.error("downloading" + zip_path_file + " failed.");
            e.printStackTrace();
        }

        // Delete this downloaded zip on exiting the app
        File tmpzip = new File(tmpdownloadzip);
        tmpzip.deleteOnExit();

        return downloadedzip;
    }


    public String unzipDownloadedZip(JPanel myComponent, String downloadedZip) {
        String unzipSuccessful = "";

        // Ask for flash drive
        String flashdrive = Utils.getFlashDrive(myComponent);
        if (!"".equals(flashdrive)) {
            // Erase flashdrive
            boolean successfully_erased = Utils.eraseFlashdrive(flashdrive);
            if (successfully_erased) {
                logger.info("successfully erased flash drive");
                // Now unzip to flashdrive
                try {
                    logger.info("downloadedZip: " + downloadedZip + " ; flashdrive: " +flashdrive);
                    String unzipresult = Utils.unzip(downloadedZip, flashdrive);
                    //unzip(downloadedZip, flashdrive);
                    logger.info(unzipresult);
                    logger.info("unzipping successful");
                    unzipSuccessful = "unzipping successful";
                } catch (IOException e) {
                    unzipSuccessful = "unzip failed";
                    logger.error("unzipping" + downloadedZip + " failed.");
                    e.printStackTrace();
                }
            } else {
                unzipSuccessful = "erasing flashdrive failed";
                logger.error("erasing flashdrive failed.");
            }

        } else {
            unzipSuccessful = "no flashdrive specified";
            logger.error("User did not specify a flash drive.");
        }
        return unzipSuccessful;
    }

    public Void which_com_syu_ms(JPanel myComponent, JProgressBar progressBar, JLabel progressText, JComboBox comboBoxmsmodsdates, JRadioButton[] getmsmodRadiobuttons) {
        //Which msmod did the user select
        String mod_type = "";
        String mod_type_text = "";

        // Retrieve selections
        String mod_date = String.valueOf(comboBoxmsmodsdates.getSelectedItem());
        logger.debug("mod_date is: " + mod_date);
        if (getmsmodRadiobuttons[0].isSelected()) { // original version
            mod_type = "org";
            mod_type_text = ResourceBundle.getBundle("Strings").getString("rbtn_original_msmods");
        } else if (getmsmodRadiobuttons[1].isSelected()) { // extended version
            mod_type = "ext";
            mod_type_text = ResourceBundle.getBundle("Strings").getString("rbtn_extended_msmods");
        } else { // nokill version
            mod_type = "nk";
            mod_type_text = ResourceBundle.getBundle("Strings").getString("rbtn_nokill_msmods");
        }
        logger.debug("mod_type is : " + mod_type);

        String[] options = {"Yes", "No"};
        String dialogText = String.format(ResourceBundle.getBundle("Strings").getString("verify_correct_msmod_text"), mod_type_text, mod_date);
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 350, dialogText, ProgramTexts.endHTML), ResourceBundle.getBundle("Strings").getString("verify_correct_msmod_title"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 0) { // Yes
            String msmod_url = MyConstants.COM_SYU_MS_URL + mod_date + "/" + mod_type + "/" + MyConstants.COM_SYU_MS_ZIP;
            logger.info("created url: " + msmod_url);

            // Create executor thread to be able to update the gui when download runs slightly longer
            Executor executor = java.util.concurrent.Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    String downloadedzip = download_Zip(msmod_url);
                    progressBar.setVisible(false);
                    progressText.setVisible(false);

                    if (!downloadedzip.contains("zip download failed")) { // download of zip from repo correct
                        String unzip_status = unzipDownloadedZip(myComponent, downloadedzip);
                         if (unzip_status == "unzip to flashdrive failed") {
                            logger.info("unzip to flashdrive failed");
                            JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("unzip_failed"), ResourceBundle.getBundle("Strings").getString("unzip_failed"), JOptionPane.WARNING_MESSAGE);
                        } else if (unzip_status == "unzipping successful") {
                            logger.info("msmod unzip sucessful");
                            JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("contentCopiedText"), ResourceBundle.getBundle("Strings").getString("contentCopiedTitle"), JOptionPane.INFORMATION_MESSAGE);
                         } else if (unzip_status == "no flashdrive specified") {
                             logger.info("User did not specify a flash drive.");
                             JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("no_flashdrive_specified"), ResourceBundle.getBundle("Strings").getString("no_flashdrive"), JOptionPane.WARNING_MESSAGE);
                         }

                    } else { // download from repo failed
                        logger.info("zip download failed");
                        JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("zip_download_failed"), ResourceBundle.getBundle("Strings").getString("zip_download_failed"), JOptionPane.WARNING_MESSAGE);
                    }

                }
            });
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    progressBar.setVisible(true);
                    progressText.setText(ResourceBundle.getBundle("Strings").getString("progressText"));
                    progressText.setForeground(Color.RED);
                    progressText.setVisible(true);
                }
            });

        } else { // choice was No
            logger.debug("wrong msmod selection obviously");
            //return "wrong selection";
        }
        return null;
    }

    public Void which_com_syu_radio(JPanel myComponent, JProgressBar progressBar, JLabel progressText, JComboBox comboBoxradiomods) {
        //Which radio mod did the user select

        // Retrieve selection
        String radio_mod = String.valueOf(comboBoxradiomods.getSelectedItem());
        logger.debug("selected radio mod: " + radio_mod);

        String[] options = {"Yes", "No"};
        String dialogText = String.format(ResourceBundle.getBundle("Strings").getString("verify_correct_radiomod_text"), radio_mod);
        int choice = JOptionPane.showOptionDialog(null, String.format(ProgramTexts.HTML, 350, dialogText, ProgramTexts.endHTML), ResourceBundle.getBundle("Strings").getString("verify_correct_radiomod_title"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 0) { // Yes
            String radiomod_url = MyConstants.COM_SYU_RADIO_URL + radio_mod + "/" + MyConstants.COM_SYU_RADIO_ZIP;
            logger.info("created url: " + radiomod_url);

            // Create executor thread to be able to update the gui when download runs slightly longer
            Executor executor = java.util.concurrent.Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    String downloadedzip = download_Zip(radiomod_url);
                    progressBar.setVisible(false);
                    progressText.setVisible(false);

                    if (!downloadedzip.contains("zip download failed")) { // download of zip from repo correct
                        String unzip_status = unzipDownloadedZip(myComponent, downloadedzip);
                        if (unzip_status == "unzip to flashdrive failed") {
                            logger.info("unzip to flashdrive failed");
                            JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("unzip_failed"), ResourceBundle.getBundle("Strings").getString("unzip_failed"), JOptionPane.WARNING_MESSAGE);
                        } else if (unzip_status == "unzipping successful") {
                            logger.info("radiomod unzip sucessful");
                            JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("contentCopiedText"), ResourceBundle.getBundle("Strings").getString("contentCopiedTitle"), JOptionPane.INFORMATION_MESSAGE);
                        } else if (unzip_status == "no flashdrive specified") {
                            logger.info("User did not specify a flash drive.");
                            JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("no_flashdrive_specified"), ResourceBundle.getBundle("Strings").getString("no_flashdrive"), JOptionPane.WARNING_MESSAGE);
                        }

                    } else { // download from repo failed
                        logger.info("zip download failed");
                        JOptionPane.showMessageDialog(myComponent, ResourceBundle.getBundle("Strings").getString("zip_download_failed"), ResourceBundle.getBundle("Strings").getString("zip_download_failed"), JOptionPane.WARNING_MESSAGE);
                    }

                }
            });
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    progressBar.setVisible(true);
                    progressText.setText(ResourceBundle.getBundle("Strings").getString("progressText"));
                    progressText.setForeground(Color.RED);
                    progressText.setVisible(true);
                }
            });

        } else { // choice was No
            logger.debug("wrong radiomod selection obviously");
            //return "wrong selection";
        }
        return null;
    }

}
