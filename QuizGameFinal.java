// Importing required Java Swing and AWT libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Class to represent one question
class Question {
    String question;              // Question text
    String[] options;             // Array of answer options
    int correctAnswerIndex;       // Correct answer position (1–4)

    // Constructor to initialize question data
    Question(String question, String[] options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }
}

// Main class (GUI + Game Logic)
public class QuizGameFinal extends JFrame implements ActionListener {

    // Variables for game logic
    Question[] questions;         // Array of all questions
    int current = 0;              // Current question index
    int score = 0;                // Player’s score
    int timeLeft = 10;            // Countdown timer (in seconds)
    String playerName = "";       // Stores player's name

    // GUI Components
    JLabel questionLabel, scoreLabel, timerLabel, nameLabel;
    JRadioButton[] options = new JRadioButton[4];   // 4 radio buttons for choices
    JButton nextButton, startButton;
    JTextField nameField;         // Field for player name input
    ButtonGroup bg;               // Groups radio buttons
    Timer timer;                  // Swing timer for countdown
    JPanel startPanel, quizPanel; // Two screens (start and quiz)

    // ------------------------ CONSTRUCTOR ------------------------
    QuizGameFinal() {
        // Setting up the window
        setTitle("Java Quiz Game");
        setSize(650, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(230, 240, 255)); // Light background color

        // ---------- START SCREEN ----------
        startPanel = new JPanel();
        startPanel.setLayout(null);
        startPanel.setBounds(0, 0, 650, 450);
        startPanel.setBackground(new Color(230, 240, 255));

        // Welcome message
        JLabel welcomeLabel = new JLabel("🎮 Welcome to the Java Quiz!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        welcomeLabel.setBounds(160, 80, 400, 40);
        startPanel.add(welcomeLabel);

        // Label asking for player name
        nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        nameLabel.setBounds(220, 150, 200, 30);
        startPanel.add(nameLabel);

        // Text field to input name
        nameField = new JTextField();
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameField.setBounds(200, 190, 220, 30);
        startPanel.add(nameField);

        // Button to start the quiz
        startButton = new JButton("Start Quiz");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        startButton.setBounds(230, 250, 160, 40);
        // When clicked, startQuiz() method runs
        startButton.addActionListener(e -> startQuiz());
        startPanel.add(startButton);

        add(startPanel); // Add start panel to frame

        // ---------- QUIZ SCREEN ----------
        quizPanel = new JPanel();
        quizPanel.setLayout(null);
        quizPanel.setBounds(0, 0, 650, 450);
        quizPanel.setBackground(new Color(230, 240, 255));
        quizPanel.setVisible(false); // Hidden until quiz starts
        add(quizPanel);

        // Label for question text
        questionLabel = new JLabel();
        questionLabel.setBounds(50, 70, 550, 30);
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        quizPanel.add(questionLabel);

        // Label for displaying score
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setBounds(520, 10, 120, 30);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        quizPanel.add(scoreLabel);

        // Label for timer countdown
        timerLabel = new JLabel("Time left: 10s");
        timerLabel.setBounds(50, 10, 150, 30);
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        timerLabel.setForeground(Color.RED); // Red for urgency
        quizPanel.add(timerLabel);

        // Group for radio buttons (so only one can be selected)
        bg = new ButtonGroup();
        int y = 120;
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton(); // Create each option
            options[i].setBounds(70, y, 450, 25);
            options[i].setFont(new Font("SansSerif", Font.PLAIN, 14));
            options[i].setBackground(new Color(230, 240, 255));
            bg.add(options[i]);   // Add to button group
            quizPanel.add(options[i]);
            y += 40;
        }

        // Button to go to next question
        nextButton = new JButton("Next");
        nextButton.setBounds(250, 300, 120, 35);
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        nextButton.addActionListener(this); // Calls actionPerformed()
        quizPanel.add(nextButton);

        // ---------- QUESTION DATA ----------
        questions = new Question[]{
                new Question("Which language is used for Android development?",
                        new String[]{"Python", "Java", "C++", "Swift"}, 2),
                new Question("What is the size of int in Java?",
                        new String[]{"2 bytes", "4 bytes", "8 bytes", "Depends on OS"}, 2),
                new Question("Which keyword is used to inherit a class?",
                        new String[]{"super", "this", "extends", "implements"}, 3),
                new Question("Which company developed Java?",
                        new String[]{"Sun Microsystems", "Microsoft", "Oracle", "Google"}, 1),
                new Question("Which of these is not a Java feature?",
                        new String[]{"Object-Oriented", "Use of pointers", "Portable", "Secure"}, 2)
        };

        // ---------- TIMER LOGIC ----------
        // This runs every 1 second (1000 ms)
        timer = new Timer(1000, e -> {
            timeLeft--; // Decrease countdown
            timerLabel.setText("Time left: " + timeLeft + "s");

            // If time runs out → automatically move to next question
            if (timeLeft == 0) {
                nextQuestion();
            }
        });

        // Make window visible
        setVisible(true);
    }

    // ------------------------ START QUIZ METHOD ------------------------
    void startQuiz() {
        playerName = nameField.getText().trim(); // Get entered name

        // Check if name field is empty
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name before starting!");
            return;
        }

        // Hide start screen and show quiz screen
        startPanel.setVisible(false);
        quizPanel.setVisible(true);

        // Display first question
        displayQuestion();

        // Start the timer
        timer.start();
    }

    // ------------------------ DISPLAY QUESTION ------------------------
    void displayQuestion() {
        // If there are still questions left
        if (current < questions.length) {
            bg.clearSelection(); // Unselect previous answers
            Question q = questions[current];
            questionLabel.setText("Q" + (current + 1) + ". " + q.question);

            // Display 4 options
            for (int i = 0; i < 4; i++) {
                options[i].setText(q.options[i]);
            }

            // Reset timer
            timeLeft = 10;
            timerLabel.setText("Time left: 10s");
        } else {
            // All questions answered → end quiz
            endQuiz();
        }
    }

    // ------------------------ MOVE TO NEXT QUESTION ------------------------
    void nextQuestion() {
        timer.stop(); // Pause timer while checking answer
        Question q = questions[current];
        int selected = -1;

        // Check which option was selected
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                selected = i + 1; // +1 to match answer index
                break;
            }
        }

        // If answer is correct → increase score
        if (selected == q.correctAnswerIndex) {
            score++;
            scoreLabel.setText("Score: " + score);
        }

        // Move to next question
        current++;

        // If it's the last question, change button text
        if (current == questions.length) {
            nextButton.setText("Finish");
        }

        // Show next question and restart timer
        displayQuestion();
        timer.start();
    }

    // ------------------------ BUTTON ACTION HANDLER ------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        nextQuestion(); // Go to next question when "Next" clicked
    }

    // ------------------------ END QUIZ ------------------------
    void endQuiz() {
        timer.stop(); // Stop countdown
        quizPanel.removeAll(); // Clear quiz screen
        quizPanel.repaint();

        // Show thank-you message
        JLabel thankYou = new JLabel("🎉 Quiz Completed!");
        thankYou.setFont(new Font("SansSerif", Font.BOLD, 22));
        thankYou.setBounds(220, 100, 400, 40);
        quizPanel.add(thankYou);

        // Show final score and player name
        JLabel result = new JLabel(playerName + ", your final score is: " + score + "/" + questions.length);
        result.setFont(new Font("SansSerif", Font.PLAIN, 18));
        result.setBounds(160, 160, 400, 30);
        quizPanel.add(result);

        // Exit button
        JButton exitBtn = new JButton("Exit");
        exitBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        exitBtn.setBounds(260, 230, 120, 35);
        exitBtn.addActionListener(e -> System.exit(0));
        quizPanel.add(exitBtn);

        quizPanel.revalidate();
        quizPanel.repaint();

        // Pop-up showing score
        JOptionPane.showMessageDialog(this,
                "Congratulations, " + playerName + "! 🎯\nYour Final Score: " + score + "/" + questions.length);
    }

    // ------------------------ MAIN METHOD ------------------------
    public static void main(String[] args) {
        new QuizGameFinal(); // Create and start the game
    }
}
