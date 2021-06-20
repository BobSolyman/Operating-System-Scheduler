import java.io.*;
import java.lang.reflect.Member;
import java.util.*;

public class OS {
    public Hashtable<String, String> Memory;

    public OS() {
        Memory = new Hashtable<>();
    }

    public String readFile(String fileName) throws IOException {
        //read the file and return it as a string
        if (readMemory(fileName)!=null){
            fileName = readMemory(fileName);
        }
        String res = "";
        try{
            FileReader file = new FileReader("src/"+fileName);
            BufferedReader br = new BufferedReader(file);
            String st;
            while ((st = br.readLine()) != null){
                res = res + st + "\n";
            }
            if (res==null){
                res = "File not found!";
            }
            return res;
        }
        catch (Exception e){
            return "File not found!";
        }

    }

    public void writeFile(String fileName, String data) throws IOException {
        if (readMemory(fileName)!=null){
            fileName = readMemory(fileName);
        }
        if (readMemory(data)!= null){
            data = readMemory(data);
        }


        FileWriter fr = new FileWriter("src/"+fileName);
        BufferedWriter br = new BufferedWriter(fr);
        String d = data;
        br.write(d);
        br.close();
        fr.close();
    }

    public void writeOutput(String print){
        String x = print;
        String v= readMemory(print);
        if(v!=null){
            x=v;
        }
        System.out.println(x);
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

    public void assign(String a, String b){

        String x = b;
        String v = readMemory(b);
        if(v!=null){
            x = v;
        }
        writeMemory(a, x);
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
            Stack <String> s = new Stack<>();
            for (int j = 0 ; j < line.length ; j++){
                s.push(line[j]);
            }
            while(!s.isEmpty()){
                String b = null ;
                String a = null ;
                String inst = null ;
                b = s.pop();
                if (b.equals("input")){
                    b = readInput() ;
                    s.push(b);
                    continue;
                }
                if (s.peek().equals("readFile")){
                    inst = s.pop();
                    s.push(readFile(b));
                    continue;
                }
                if (s.peek().equals("print")){
                    inst = s.pop();
                    writeOutput(b);
                    continue;
                }
                a = s.pop();
                if (s.peek().equals("assign")){
                    inst = s.pop();
                    assign(a, b);
                    continue;
                }
                if (s.peek().equals("add")){
                    inst = s.pop();
                    add(a, b);
                    continue;
                }
                if (s.peek().equals("writeFile")){
                    inst = s.pop();
                    writeFile(a, b);
                    continue;
                }


            }// end of stack


        }// end of loop of program

    }// end of parse method

    public static void main(String[] args) {
        OS x = new OS();
        try {
            System.out.println("Execution of Program 1");
            x.parser("Program 1.txt");
            System.out.println("***********************");
            System.out.println("Execution of Program 2");
            x.parser("Program 2.txt");
            System.out.println("***********************");
            System.out.println("Execution of Program 3");
            x.parser("Program 3.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }// end of main

}
