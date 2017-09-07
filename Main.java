import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;



public class Main {

	public static void main(String[] args) {
		String output="";
		Scanner scan = new Scanner(System.in);
		int num_knights = scan.nextInt();
		int iterations = scan.nextInt();
		Coordinate queen = new Coordinate(scan.nextInt(), scan.nextInt());
		ArrayList<Knight> knights = new ArrayList<Knight>();
		
		
		for(int i=1; i<num_knights+1; i++){
			try {
				String str = "./src/" + String.valueOf(i) + ".txt";
				List<String> lines = Files.readAllLines(Paths.get(str));
				
				String name = lines.get(0);
				String[] data=lines.get(1).split("\\s");
				int x = Integer.parseInt(data[0]);
				int y = Integer.parseInt(data[1]);
				Knight knight = new Knight(x,y,name);
				
				int m = Integer.parseInt(lines.get(2));
				for(int j=1; j<m+1; j++){
					data=lines.get(2+j).split("\\s");
					if(data.length>2){
						Coordinate c = new Coordinate(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
						knight.add(c);
					}
					
					else{
						knight.add(data[1]);
					}
				}
				
				knights.add(knight);
				
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		scan.close();
		Collections.sort(knights);
		
		int iter=1;
		while(iter<=iterations){
			for(int i=0; i<knights.size(); i++){
				
				Knight curr = knights.get(i);
				output+=iter+" "+curr.getName()+" "+curr.getPos().toString()+"\n";
				
				try{
					if(curr.isEmpty()==false){
						Object o1 = curr.pop();
						try{
							if(o1 instanceof Coordinate){
								Coordinate c  = (Coordinate) o1;
								curr.setPos(c);
								try{
									for(int j=0; j<knights.size()+1; j++){
											if(j==0 && curr.getPos().equals(queen)){
												throw new QueenFoundException();
											
											}
											else if(j>0 && j-1!=i && curr.getPos().equals(knights.get(j-1).getPos())){
												String name = knights.get(j-1).getName();
												knights.remove(j-1);
												throw new OverLapException(name);
											}
															
									}
									output+="No exception "+curr.getPos().toString()+"\n";
								}
								catch(QueenFoundException e){
									output+=e.getMessage()+"\n";
									iter=iterations+1;
									i=knights.size()+1;
									break;
								}
								catch(OverLapException e){
									output+=e.getMessage()+"\n";
								}
								
								
								
							}
							else throw new NonCoordinateException(o1.toString());
						}
						catch(NonCoordinateException e){
							output+=e.getMessage()+"\n";
						}
						
					}
					else throw new StackEmptyException();				
				}
				catch (StackEmptyException e){
					output+=e.getMessage()+"\n";
					knights.remove(curr);
				}
			}

			iter++;
			
		}
		
		try{
		    PrintWriter writer = new PrintWriter("./src/output.txt", "UTF-8");
		    	 writer.printf(output);
		    	 
		    
		    writer.close();
		} 
		catch (IOException e) {
		  
		}
		
	}

	

}



class Knight implements Comparable<Knight>{
	protected Stack<Object> magicBox = new Stack<Object>();
	protected Coordinate coord;
	final protected String name;
	
	
	public Knight(int a, int b, String x){
		this.name = x;
		this.coord = new Coordinate(a,b);
		
	}
	
	public void add(Object a){
		this.magicBox.push(a);
	}
	
	public Object pop(){
		return this.magicBox.pop();
	}
	
	public boolean isEmpty(){
		return this.magicBox.empty();
	}
	
	public String getName(){
		return this.name;
	}
	
	public Coordinate getPos(){
		return this.coord;
	}
	
	public void setPos(Coordinate a){
		this.coord = a;
	}
	

	@Override
	public int compareTo(Knight o) {
		String b = o.getName();
		return this.name.compareTo(b);
	}

}

class Coordinate{
	private final int[] coord;
	public Coordinate(int a, int b){
		int[] inp = {a,b};
		this.coord =inp;
	}
	
	public int[] getCoord(){
		return this.coord;
	}
	
	@Override
	public boolean equals(Object o){
		Coordinate c = (Coordinate) o;
		int x = c.getCoord()[0];
		int y = c.getCoord()[1];
		if(x==this.coord[0] && y==this.coord[1]){
			return true;
		}
		
		else return false;
		
	}
	
	public String toString(){
		return (this.coord[0]+" "+this.coord[1]);
	}
}


class NonCoordinateException extends Exception{
	
	private final String s;
	public NonCoordinateException(String s){
		super(s);
		this.s = s;
	}
	
	@Override
	public String getMessage(){
		return ("NonCoordinateException: Not a coordinate Exception "+s);
	}
	
	
	
}


class StackEmptyException extends Exception{
	
	@Override
	public String getMessage(){
		return ("StackEmptyException: Stack Empty exception");
	}
	
}

class QueenFoundException extends Exception{
	
	@Override
	public String getMessage(){
		return ("QueenFoundException:​ ​Queen​ ​has​ ​been​ ​Found.​ ​Abort!");
	}
}

class OverLapException extends Exception{
	String s;
	public OverLapException(String s){
		super(s);
		this.s = s;
	}
	
	@Override
	public String getMessage(){
		return ("OverlapException:​ ​Knights​ ​Overlap​ ​Exception "+s);
	}
	
}
