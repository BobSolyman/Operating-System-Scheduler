import java.io.*;
import java.lang.reflect.Member;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Vector;

public class Main {
    public Hashtable<String, String> Memory;

    public String readFile(String fileName) throws IOException {
        //read the file and return it as a string
        String res = "" ;
        File file = new File("src/"+fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null){
            res = res + st + "/n";
        }
        return res;
    }

    public void writeFile(String fileName, String data) throws IOException {
        FileWriter fr = new FileWriter("src/"+fileName);
        BufferedWriter br = new BufferedWriter(fr);
        String d = data;
        br.write(d);
        br.close();
        fr.close();
    }

    public void writeOutput(String print){
        System.out.println(print);
    }

    public String readInput(){
        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();
        return userInput;
    }

    public String readMemory(String x){
        return Memory.get(x);
    }

    public void writeMemory(String assigned, String assignee){
        Memory.put(assigned, assignee);
    }

    public void assign(Vector<String> data){
        String x = data.get(1);
        String v = readMemory(data.get(1));
        if(data.size()==3 && data.get(1).equals("readFile")){
            try {
                x = readFile(data.get(2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(data.get(1).equals("input")){
            x = readInput();
        }
        else if(v!=null){
            x = v;
        }
        writeMemory(data.get(0), x);
    }

    public void add(String assigned, String assignee){
        String x = assignee;
        String v = readMemory(assignee);
        if(readMemory(assigned)==null){
            try {
                writeFile(assigned, "0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(v!=null){
            x= v;
        }
        int y = Integer.parseInt(readMemory(assigned))+Integer.parseInt(x);
        writeMemory(assigned, y+"");
    }

    public void parser(String program) throws IOException {

        File file = new File("src/"+program);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null){
            String[] line = st.split(" ");
            int skipper = 0 ;
            String inst = "";
            Vector<String> instruction = new Vector<>();
            for (int j = 0 ; j < line.length ; j++){

                switch (line[j]) {
                    case "assign":
                        skipper = skipper + 2;
                        if (inst=="")
                            inst = "assign";
                        break;
                    case "print":
                        skipper++;
                        if (inst=="")
                            inst = "print";
                        break;
                    case "add":
                        skipper = skipper + 2;
                        if (inst=="")
                            inst = "add";
                        break;
                    case "readFile":
                        skipper++;
                        if (inst=="")
                            inst = "readFile";
                        break;
                    case "writeFile":
                        skipper = skipper + 2;
                        if (inst=="")
                            inst = "writeFile";
                        break;
                    default:
                        instruction.add(line[j]);
                        skipper--;
                        break;
                } // end of switch for reading line ;

                if (skipper == 0){
                    switch (inst){
                        case "assign":
                            assign(instruction);
                            break;
                        case  "print":
                            writeOutput(instruction.get(0));
                            break;
                        case "add":
                            add(instruction.get(0),instruction.get(1));
                            break;
                        case "readFile":
                            readFile(instruction.get(0));
                            break;
                        case "writeFile":
                            writeFile(instruction.get(0),instruction.get(1));
                            break;
                    }//end of switch to choose which instruction

                }// end of if to execute line

            }// end of looping in line

        }// end of loop of program

    }// end of parse method

    public static void main(String[] args) {
        Hashtable<String, String> x= new Hashtable<>();

        System.out.println(x.get("r"));
    }

}
