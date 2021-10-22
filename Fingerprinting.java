import com.opencsv.CSVWriter;

import java.io.*;
import java.util.LinkedList;

public class Fingerprinting {

    public static void doFingerprinting(){
        LinkedList<String[]> id_coords = new LinkedList<String[]>();
        String[] temp = new String[3];
        //command line file grabber
       /*
        if((args.length) != 2){
            System.out.printf("Invalid number of arguments: %d given, needs 2.\n\nProvide input and output file name", args.length);

        }


        else
        {
            //grab from command line
            String pathToCsv=args[1];
        }
        */

        LinkedList<LinkedList<String[]>> LLParent = new LinkedList<>();

        int ID=0;
        try {
            String pathToCsv = "RSSI.csv";
            BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));


            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");

                //holds tuple
                String[] xy = {data[1], data[2]};
                String[] tempxy = new String[2];




                int beaconNum;
                //loops through .csv row starting from 4th position
                for (int i = 3; i < data.length - 1; i++) {
                    int match = 0;
                    beaconNum = i;
                    int done=0;
                    int j = 0;
                    //if value is filtered, ignore



                    if (data[i].equals("\"-1000\""))
                    {
                        //System.out.println("filter is active: " + data[i]);

                    }
                    else{
                        //System.out.println("filter is inactive: " + data[i]);

                        while (LLParent.size()>j) {
                                /*
                                if (LLParent.get(j).size()==0){
                                    continue;
                                } */

                            //System.out.printf("j is :%d \nLLparent size is %d\n",j, LLParent.size());
                            tempxy[0] = LLParent.get(j).get(0)[1];
                            tempxy[1] = LLParent.get(j).get(0)[2];



                            //if the x and y coords of the data
                            if (tempxy[0].equals(xy[0]) && tempxy[1].equals(xy[1])) {
                                match = 1;
                                break;
                            }

                            j++;
                        }





                        //if there is a match
                        if (match == 1) {
                            //check if the significant column is populated

                            for (int k = 0; k < LLParent.get(j).size(); k++) {
                                //if there is an open spot in any of the previously made lists... add it!
                                if (LLParent.get(j).get(k)[beaconNum]== null) {
                                    LLParent.get(j).get(k)[beaconNum] = data[beaconNum];
                                    done = 1;
                                    break;
                                }
                            }

                            if (done==0) {


                                String[] stringo = new String[36];
                                stringo[0] = String.valueOf(ID);
                                stringo[1] = tempxy[0];
                                stringo[2] = tempxy[1];
                                stringo[beaconNum] = data[beaconNum];

                                //append string to llchild for more room
                                LLParent.get(j).add(stringo);
                            }


                        }
                        //if not a match
                        else {
                            //make new linked list corresponding to new (x,y)
                            LinkedList<String[]> list = new LinkedList<>();
                            LLParent.add(list);
                            //generate array that will be first element in the new linked list, populate it
                            String[] dataRow = new String[37];

                            ID++;



                            dataRow[0] = Integer.toString(ID);
                            dataRow[1] = xy[0];
                            dataRow[2] = xy[1];



                            String[] written={dataRow[0].toString(),dataRow[1].replace("\"", ""),dataRow[2].replace("\"", "")};
                            id_coords.add(written);


                            dataRow[beaconNum] = data[beaconNum];
                            LLParent.getLast().add(dataRow);

                            //The corresponding (x,y) pair now has its own linked list, which has one element containing the first iteration of the (x,y) pair
                        }

                    }

                }
                //get (x, y)
                //get beaconData (find value that's not -1000)
                //traverse LLParent to find (x, y) match
                // CASE 1 - no match, make new LLChild, insert beaconData
                // CASE 2 - yes match (x, y), no match beacon, insert beaconData
                // CASE 3 - yes match (x, y), yes match beacon, make new LLChild, insert beaconData


            }


            //make .csv



            //System.out.println(LLParent.get(0).get(0)[0]);
            CSVWriter writer = new CSVWriter(new FileWriter("output.csv"));

            //header goes here...
            //String header = "x,y"
            //for loop up to 36 that appends ",\"i\"" to the end of the string



            for (LinkedList<String[]> LLchild : LLParent) {
                for (String[] string : LLchild) {
                    int n=0;

                    for (String i : string) {

                        if (i!=null) {

                            string[n] = i.replace("\"", "");
                        }
                        n++;
                    }

                    writer.writeNext(string);
                }

            }


            csvReader.close();


            File file = new File("id_to_coords.csv");
            try {
                // create FileWriter object with file as parameter
                FileWriter outputfile = new FileWriter(file);

                // create CSVWriter object filewriter object as parameter
                CSVWriter secondarywriter = new CSVWriter(outputfile);

                // adding header to csv
                String[] header = { "ID", "X", "Y" };
                secondarywriter.writeNext(header);

                for (int k = 0; k < id_coords.size(); k++) {
                    secondarywriter.writeNext(id_coords.get(k));
                }
                // add data to csv


                // closing writer connection
                secondarywriter.close();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


















        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    }

