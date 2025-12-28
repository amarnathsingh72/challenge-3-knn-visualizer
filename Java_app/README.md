Hey! Everyone "I'm K-NN Algorithum Visualizer"
Here I will be telleng you:-
1. Why I chose this topic..?
    
    I made this project to unnderstand, How K-NN algorithm works in visual way. Rather then studying theory, books and all, What i say is with theory Knowledge practical learning is the best way.
2.  What is K-NN.?
    
     You have many labeled points (here: blue = Class A, red = Class B).You get a new point (green) and want to know its class.Find the k closest points to it Count how many are blue and how many are red.Whichever is more, that becomes the prediction.If the counts are equal, it can be treated as a tie.
    
3.  How app works.?

      Main.java:-  
      Starts the program and opens the main window with the title and a button “Start k‑NN Demo”.

      MainPanel.java
        Shows the heading and the “Start k‑NN Demo” button.
        When you click the button, it opens a new window with the actual k‑NN visualizer.

      KNNCanvas.java
        This is where all the action happens. It has:A drawing area (grid, points, query point, yellow lines, decision boundary).A control panel with:k slider[“Add Query Point & Predict”,“Clear All”,“Explain with AI”] button.
4.   How to run.?
  
       Backend-->   cd backend
                    python app.py

       Frontend-->  javac *.java
                    java Main 

        
        Then:

                        Click “Start k‑NN Demo”.

                        Add red and blue points by clicking.

                        Adjust k with the slider.

                        Click “Add Query Point & Predict” to see neighbors and prediction.

                        Click “Explain with AI” to read the explanation.

                        Use “Clear All” to start again.