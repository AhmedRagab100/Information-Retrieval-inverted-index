
import java.io.*;
import java.util.*;

//=====================================================================
class DictEntry2 {

    public int doc_freq = 0; // number of documents that contain the term
    public int term_freq = 0; //number of times the term is mentioned in the collection
    public HashSet<Integer> postingList;

    DictEntry2() {
        postingList = new HashSet<Integer>();
    }
}

//=====================================================================
class Index2 {

    //--------------------------------------------
    Map<Integer, String> sources;  // store the doc_id and the file name
    HashMap<String, DictEntry2> index; // THe inverted index
    //--------------------------------------------

    Index2() {
        sources = new HashMap<Integer, String>();
        index = new HashMap<String, DictEntry2>();
    }

    //---------------------------------------------
    public void printDictionary() {
        Iterator it = index.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            DictEntry2 dd = (DictEntry2) pair.getValue();
            HashSet<Integer> hset = dd.postingList;// (HashSet<Integer>) pair.getValue();
            System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "] <" + dd.term_freq + "> =--> ");
            Iterator<Integer> it2 = hset.iterator();
            while (it2.hasNext()) {
                System.out.print(it2.next() + ", ");
            }
            System.out.println("");
            //it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("------------------------------------------------------");
        System.out.println(" Number of terms = " + index.size());
        
        System.out.println("------------------------------------------------------");
    }

    //-----------------------------------------------
    public void buildIndex(String[] files) {
        int i = 0;
        for (String fileName : files) {
            try ( BufferedReader file = new BufferedReader(new FileReader(fileName))) {
                sources.put(i, fileName);
                String ln;
                while ((ln = file.readLine()) != null) {
                    String[] words = ln.split("\\W+");
                    for (String word : words) {
                        word = word.toLowerCase();
                        // check to see if the word is not in the dictionary
                        if (!index.containsKey(word)) {
                            index.put(word, new DictEntry2());
                        }
                        // add document id to the posting list
                        if (!index.get(word).postingList.contains(i)) {
                            index.get(word).doc_freq += 1; //set doc freq to the number of doc that contain the term 
                            index.get(word).postingList.add(i); // add the posting to the posting:ist
                        }
                        //set the term_fteq in the collection
                        index.get(word).term_freq += 1;
                    }
                }
                printDictionary();
            } catch (IOException e) {
                System.out.println("File " + fileName + " not found. Skip it");
            }
            i++;
        }
    }

    //----------------------------------------------------------------------------  
    HashSet<Integer> intersect(HashSet<Integer> pL1, HashSet<Integer> pL2) {
        HashSet<Integer> answer = new HashSet<Integer>();;
        List<Integer> List1= new ArrayList<Integer>(pL1);
        List<Integer> List2= new ArrayList<Integer>(pL2);
        int i=0 , j=0;
        while(i != List1.size() && j != List2.size()) {
        	
        	if(List1.get(i)<List2.get(j))
        		i++;
        	
        	else if(List1.get(i)>List2.get(j))	
        		j++;
        		
        	else {
        		answer.add(List1.get(i));
        		i++;
        		j++;
        	}
        }
        
        
		return answer;
    }
    
    public String find(String phrase) {
        String[] words = phrase.split("\\W+");
        HashSet<Integer> res = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        for (String word : words) {
            res.retainAll(index.get(word).postingList);
        }
        
        if (res.size() == 0) {
            System.out.println("Not found");
            return "";
        }
      
        String result = "Found in: \n";
        for (int num : res) {
            result += "\t" + sources.get(num) + "\n";
        }
        return result;
    }
    //-----------------------------------------------------------------------   
    
 /* HashSet<Integer> OR(HashSet<Integer> pL1, HashSet<Integer> pL2) {
        HashSet<Integer> answer = new HashSet<Integer>();;
        List<Integer> List1= new ArrayList<Integer>(pL1);
        List<Integer> List2= new ArrayList<Integer>(pL2);
        
        int i=0 , j=0;
       while(i != List1.size() && j != List2.size() ) {
   
           if(list1.get(i)>list2.get(j)) { 

                answer.add(list2.get(j));
                 j++;
           }    
    	  else if(list1.get(i)<list2.get(j)){

               answer.add(list1.get(i));
                 i++;
         }

          else {
              j++;
          }
     }

   if(i <= list1.size()) {
       
       answer.add(list1.get(i));
    }

   else if(j <= list2.size()) {
       
       answer.add(list2.get(j));
    }
       
      	return answer;
 }
*/ 
    
    HashSet<Integer> OR(HashSet<Integer> pL1, HashSet<Integer> pL2) {                  
        HashSet<Integer> answer = new HashSet<Integer>();
        List<Integer> List1= new ArrayList<Integer>(pL1);
        List<Integer> List2= new ArrayList<Integer>(pL2);
                                                                                 
        int i=0 , j=0;
       while(i != List1.size()) {
    	   answer.add(List1.get(i));
    	   i++;
       }
       
       while(j != List2.size()) {
    	   answer.add(List2.get(j));
    	   j++;
       }
        
		return answer;
 }
 //----------------------------------------------------------------------- 
    
 HashSet<Integer> Not(HashSet<Integer> pL) {
   //answer that have all files in index     
        HashSet<Integer> answer = new HashSet<Integer>(sources.keySet());
        List<Integer> List= new ArrayList<Integer>(pL);

        for (int i = 0; i < pL.size(); i++) {
            if (answer.contains(List.get(i))) {
                answer.remove(List.get(i));    //remove pl from answer
            }
        }

        return answer;
    }
    
 //-----------------------------------------------------------------------

    public String And2Lists(String phrase) { // 2 term phrase  (And)
        String result = "";
        String[] words = phrase.split("\\W+");
        // 1- get first posting list
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        // 2- get second posting list
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        // 3- apply the algorithm
        
        HashSet<Integer> answer = intersect(pL1, pL2);
        
        if(answer.size() == 0) {
        	System.out.println("No lists common");
        }
        
        else {
        System.out.println("Found in: ");
        
        for (int num : answer) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }
    }
        return result;
    }
    
   
//--------------------------------------------------------------------------------------------------         

    public String NotList(String phrase) { // 3 lists
        String result = "";
        String[] words = phrase.split("\\W+");
        // 1- get first posting list
        HashSet<Integer> pL = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        HashSet<Integer> answer = Not(pL);
        
       System.out.println("Found in: ");
        
        for (int num : answer) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }
        
        return result;

    }
    
    public String OR2Lists(String phrase) { // 2 term phrase  (And)
        String result = "";
        String[] words = phrase.split("\\W+");
        // 1- get first posting list
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        // 2- get second posting list
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        // 3- apply the algorithm
        
        HashSet<Integer> answer = OR(pL1, pL2);
      
        System.out.println("Found in: ");
        
        for (int num : answer) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }
        return result;
    }
    
    public String And_OR(String phrase) { // 3 lists
    	 
        String result = "";
        String[] words = phrase.split("\\W+");
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        //printPostingList(pL1);
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[2].toLowerCase()).postingList);
        //printPostingList(pL2);
        HashSet<Integer> answer1 = OR(pL1, pL2);
        //printPostingList(answer1);
        HashSet<Integer> pL3 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);        
        //printPostingList(pL3);
        HashSet<Integer> answer2 = intersect(pL3, answer1);
        if(answer2.size()==0) {
        	System.out.println("No common Lists");
        }
        
        else {
       result = "Found in: \n";
        for (int num : answer2) {
            //System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }     
      }
        return result;

    }
    
    public String And_OR_Not(String phrase) { // 3 lists
   	 
        String result = "";
        String[] words = phrase.split("\\W+");
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[2].toLowerCase()).postingList);
        HashSet<Integer> answer1 = Not(pL1);
        
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
       
        HashSet<Integer> answer2 = OR(pL2, answer1);
     
        HashSet<Integer> pL3 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);        
       
        HashSet<Integer> answer3 = intersect(pL3, answer2);
        if(answer3.size()==0) {
        	System.out.println("No common Lists");
        }
        
        else {
       result = "Found in: \n";
        for (int num : answer3) {
           
            result += "\t" + sources.get(num) + "\n";
        } 
       }
        return result;

    }
    
    public String And_OR_And_Not(String phrase) { // 4 lists
      	 
        String result = "";
        String[] words = phrase.split("\\W+");
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[3].toLowerCase()).postingList);
        HashSet<Integer> answer1 = Not(pL1);
        
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[2].toLowerCase()).postingList);

        HashSet<Integer> pL3 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        HashSet<Integer> pL4 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);        
        
        HashSet<Integer> answer2 = OR(pL2, pL3);
       
        HashSet<Integer> answer3 = intersect(pL4, answer2);
        
        HashSet<Integer> answer4 = intersect(answer1, answer3);
        
        if(answer4.size()==0) {
        	System.out.println("No common Lists");
        }
        else {
       result = "Found in: \n";
        for (int num : answer4) {
           
            result += "\t" + sources.get(num) + "\n";
        }
    }
        return result;

    }
   
   
}
