import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.openxml4j.opc.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import javax.swing.JOptionPane;

import java.time.*;
import java.time.format.DateTimeFormatter;
public class convertToWord {
	public String dirName;
	public  String fileName = "TableTennisStations.docx";
	public final String docTitle = "CISS Table Tennis Stations";
	private File doc;
//	public static void main(String[] args) {
//		convertToWord document = new convertToWord(null, LocalDateTime.now());
//		
//	}
	public convertToWord(FinalStationMasterList stationList, LocalDateTime dateTime, String docDirName) {
		dirName = docDirName;
		fileName = "TableTennisStations" + dateTime.toString() + ".docx";
		File dir = new File(dirName);
		if(!dir.exists()) dir.mkdir();
		FileOutputStream outStream = makeOutStream(dir.getAbsolutePath() + "/" + fileName);
		doc = new File(dir.getAbsolutePath() + "/" + fileName);
		XWPFDocument doc = new XWPFDocument();
		
		XWPFParagraph title = doc.createParagraph();
		title.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun titleRun = title.createRun();
		titleRun.setBold(true);
		titleRun.setFontSize(24);
		titleRun.setText(docTitle);
		
		XWPFParagraph time = doc.createParagraph();
		time.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun timeRun = time.createRun();
		timeRun.setFontSize(14);
		timeRun.setText(dateTime.getMonth() + " " + dateTime.getDayOfMonth());
		timeRun.addBreak();
		for(FinalStation station : stationList.getFinalStations()) {
			writeStation(doc, station);
			XWPFParagraph blank = doc.createParagraph();
		}
		writeToFile(outStream, doc);
	}
	public void writeStation(XWPFDocument doc, FinalStation station) {
		XWPFParagraph stationName = doc.createParagraph();
		stationName.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun stationNameRun = stationName.createRun();
		stationNameRun.setFontSize(12);
		stationNameRun.setText(station.getStation().getStationName());
		
		int playerNumber = station.getCurrentPlayers().size();
		int Rows = playerNumber/4;
		if(playerNumber % 4 > 0) {
			Rows++;
		}
		playerNumber %= 4;
		int columns = station.getCurrentPlayers().size();
		if(columns > 4)
			columns = 4;
		XWPFTable table = doc.createTable();
		CTTblLayoutType type = table.getCTTbl().getTblPr().addNewTblLayout();
		type.setType(STTblLayoutType.FIXED);
		table.setWidth("100%");
		int rowCount = 0;
		int cellCount = 0;
		for(Player player : station.getCurrentPlayers()) {
			XWPFTableRow row = table.getRow(rowCount);
			if(cellCount >= 4) {
				rowCount++;
				table.createRow();
				cellCount = 0;
				row = table.getRow(rowCount);
			}
			if(row.getCell(cellCount) == null) row.addNewTableCell();
			row.getCell(cellCount).setText(player.getName());
			int cellWidthPCT = 0;
			if(rowCount < (Rows - 1)) {
				if(station.getCurrentPlayers().size() >= 4) {
					cellWidthPCT = 25;
				} else {
					cellWidthPCT = 100/station.getCurrentPlayers().size();
				}
			}
			System.out.println(cellWidthPCT);
			row.getCell(cellCount).setWidth(Integer.toString(cellWidthPCT) + "%");

			//row.getCell(cellCount).setWidth("25%");
			row.getCell(cellCount).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
		    cellCount++;
		}
	}
	public void writeToFile(FileOutputStream outStream, XWPFDocument doc) {
		try {
			doc.write(outStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public FileOutputStream makeOutStream(String path) {
		try {
			FileOutputStream outStream = new FileOutputStream(path);
			return outStream;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Could not initialize outputStream");
			System.out.println("generating outStream doesn't work");
			return null;
		}
	}
	public File getDoc() {
		return doc;
	}
}
