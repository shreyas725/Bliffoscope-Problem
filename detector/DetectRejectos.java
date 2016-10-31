package detector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DetectRejectos {
	final static String inputFileNames[] = {
		"SlimeTorpedo.blf",
		"Starship.blf"
	};
	
	class TargetInfo {
		public int type;
		public int rowPos, colPos;
		public float confidence;
		
		public TargetInfo(int _type, int _rowPos, int _colPos, float _confidence) {
			type = _type;
			rowPos = _rowPos;
			colPos = _colPos;
			confidence = _confidence;
		}
		
		public void printToConsole() {
			if(type == 0) System.out.print("Slime Torpedo, ");
			else System.out.print("Starship, ");
			System.out.print("(" + colPos + ", " + rowPos + "), ");
			System.out.print(confidence + "\n");
		}
	}
	
	ArrayList<ArrayList<String>> targets = new ArrayList<ArrayList<String>>(inputFileNames.length);
	ArrayList<TargetInfo> targetInfo = new ArrayList<TargetInfo>();
	ArrayList<String> testImg = new ArrayList<String>();
	
	public DetectRejectos(String testInputFileName) {
		
		BufferedReader br = null;
		String sCurrentLine;
		
		// Read target img data
		for(int i = 0; i < inputFileNames.length; ++i) {
			
			try {
				br = new BufferedReader(new FileReader(inputFileNames[i]));
				targets.add(new ArrayList<String>());
				while ((sCurrentLine = br.readLine()) != null) {
					targets.get(i).add(sCurrentLine);
					//System.out.println(targets.get(i).get(targets.get(i).size()-1));
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		// Read the test img data
		try {
			br = new BufferedReader(new FileReader(testInputFileName));
			
			while ((sCurrentLine = br.readLine()) != null) {
				testImg.add(sCurrentLine);
				//System.out.println(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	public void detect(float confidenceThreshold) {
		bruteForce(confidenceThreshold);
	}
	public void bruteForce(float confidenceThreshold) {
		int testImgRows = testImg.size();
		int testImgCols = testImg.get(0).length();
		
		for(int i = 0; i < targets.size(); ++i) {
			ArrayList<String> targetImg = targets.get(i);
			int targetImgRows = targetImg.size();
			int targetImgCols = targetImg.get(0).length();
			float fullMatch = targetImgRows * targetImgCols;
			float matchCount = 0;
			float confidence = 0;
			
			// compare and compute match on 'every cell' of the input test img.
			for(int j = 0; j <= testImgRows - targetImgRows; ++j) {
				for(int k = 0; k <= testImgCols - targetImgCols; ++k) {
					matchCount = 0;
					for(int l = 0; l < targetImgRows; ++l) {
						for(int m = 0; m < targetImgCols; ++m) {
							if(targetImg.get(l).charAt(m) == testImg.get(j + l).charAt(k + m)) ++matchCount;
						}
					}
					// take targets with confidence >= confidenceThreshold
					confidence = matchCount/fullMatch;
					if(confidence >= confidenceThreshold) {
						targetInfo.add(new TargetInfo(i, j, k, confidence));
					}
				}				
			}
		}
	}
	public void printToConsole() {
		System.out.println("Maximum 100 entries will be shown\n");
		for(int i = 0; i < (targetInfo.size() < 100 ? targetInfo.size() : 100); ++i) {
			targetInfo.get(i).printToConsole();
		}
	}
}