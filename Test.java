import detector.DetectRejectos;
class Test {
	public static void main(String argv[]) {
		
		String fileName = "TestData.blf";
		float defaultConfThd = 0.75f;
		System.out.println("Confidence scale is from 0 to 1, 0 - no match, 1 - full match");
		System.out.println("Default test file name: TestData.blf");
		System.out.println("Default confidence threshold: 0.75");
		
		try {
			fileName = argv[0];
		} catch(Exception e) {
			System.out.println("A test file name was not specified the default will be used.");
		}
		
		try {
			defaultConfThd = Float.parseFloat(argv[1]);
		} catch(Exception e) {
			System.out.println("A confidence threshold was not specified the default will be used.");
		}
		DetectRejectos dr = new DetectRejectos(fileName);
		dr.detect(defaultConfThd);
		
		System.out.println();
		dr.printToConsole();
	}
}