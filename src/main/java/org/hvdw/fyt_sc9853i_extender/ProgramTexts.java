package org.hvdw.fyt_sc9853i_extender;

public class ProgramTexts {
    /* HTML in Swing components follows the HTML 3.2 standard from 1996. See https://www.w3.org/TR/2018/SPSD-html32-20180315/
    All strings use "internal" tags, but not begin and end tags as we use the String.format(ProgramTexts.HTML, <width>, helptext)
     */
    public static final String Author = "Harry van der Wolf";
    public static final String ProjectWebSite = "http://hvdwolf.github.io/FYT_SC9853i_Extender";
    public static final String Version = "1.00";
    public static final String HTML = "<html><body style='width: %1spx'>%1s";
    public static final String endHTML = "</body></html>";
    public static final String newVersionText = "<html><big>There is a new version available</big><br><br>"
            +"I can open the releases webpage so you can download the new version.<br><br>"
            +"Open the website or not?";
    public static final String LatestVersionText = "<html>You are already using the latest version.</html>";
    public static final String aboutText =
            "<big>FYT_SC9853i_Extender</big><hr><hr>"
            +"<strong>FYT_SC9853i_Extender</strong> is a java/Swing program that contains a "
            +"number of tools to create a flashable USB-stick or SD-card to enhance/alter your Joying FYT SC9853i "
            +"unit or FYT SC9853i compatible unit.<br><br><br>"
            +"<br>This FYT_SC9853i_Extender program is free, Open Source software: you can redistribute it and/or "
            +"modify it under the terms of the GNU General Public License "
            +"as published by the Free Software Foundation, either version "
            +"3 of the License, or (at your option) any later version."
            +"<br>This program is distributed in the hope that it will be useful, "
            +"but WITHOUT ANY WARRANTY; without even the implied "
            +"warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR "
            +"PURPOSE.  See the GNU General Public License for more details."
            +"<br>You should have received a copy of the GNU General Public "
            +"License along with this program.  If not, see <a href='http://www.gnu.org/licenses'>www.gnu.org/licenses</a>.</p>"
            +"<br><br>FYT_SC9853i_Extender version: " + Version + "."
            +"<br><br>Author/creator of FYT_SC9853i_Extender: " + Author + ".";
    public static final String genInfoText =
            "<big>General Information</big><hr><br><br>"
            +"<strong>This is only for SC9853i Joying/FYT 8.1 units.</strong><br>"
            +"Due to some changes in the flashing process since the previous PX5 and Sofia 3GR models it is now partly possible to mod your unit without root.<br><br>"
            +"<strong>Possible applications:</strong>"
            +"<ul><li>Making patches, an alternative to creating the Allapp.pkg package.</li>"
            +"<li>Work on all folders in the unit (which Allapp.pkg cannot do).</li>"
            +"<li>Delete unnecessary files or applications (who uses the calculator app?), or overwite files like the fyt.prop or others.</li>"
            +"<li>Modify/overwrite config files that are otherwise not modifiable (like bluetooth config?)</li>"
            +"<li>Setting properties that normally require root/admin rights.</li>"
            +"<li>Creating backups of your partitions using the dd command. This can be handy on a device for which you do not have a firmware yet as backup.</li></ul>"
            +"<strong>What does this Java app do?</strong?<br>"
            +"<ol><li>Copy the lsec6521update executable binary in the root of the USB-stick.</li>"
            +"<li>Create the lsec_updatesh folder in the root of the USB-stick.</li>"
            +"<li>Create/copy the relevant lsec.sh script into this lsec_updatesh folder. The lsec.sh script is a linux shell script. This shell scipt does the \"magic\" after booting with the flash drive.</li>"
            +"<li>Sometimes copy additional files to the flash drive like mods that will be installed using the lsec.sh script.</li>"
            +"</ol>Result: at the end of the flashing, the lsec.sh script will be executed. Then the unit will be rebooted.<br><br>"
            +"For more info see this post on the XDA forum: https://forum.xda-developers.com/showpost.php?p=82485735&postcount=228";
    public static final String Credits =
            "<big>Credits</big><hr><br><br>"
            +"David Däster: He is not active in the XDA forum and neither has he directly contributed to this app, but he taught me so much "
            +"on another Java PC project and portions of that I used in this project (although I clumsily might have broken some golden rules "
            +"he has told me earlier ;) )<br><br>";
}
