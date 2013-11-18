import org.jdesktop.xswingx.PromptSupport;
import twitter4j.TwitterException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import javax.swing.*;
import javax.swing.border.BevelBorder;

//class starts
class AddFormWin {
    public final String APPKEY = "VSllxPEPcOWdfIWZvZ8hBA";
    public final String APPSECRET = "HYSw9rYxIAcmvaUE5T4YN0RZVFJp8W7Qa09BCgBjxCg";
    private static final int SMALL_ROWS = 5; // !! was 20!
    private static final int BIG_ROWS = 10; // !! was 50!

    private ArrayList<Account> accounts;
    private static ArrayList<String> tagIDList;
    private static ArrayList<String> tweetList;
    private static Stack<String> mentionStack;
    public static int nAccounts = 0;
    public static int nThreads = 0;
    JFrame f1;
    JTextField txtName, txtCity, txtState, txtMail;
    JButton btnAddAcc;
    JPanel p1, p2, a1, footer;
    GridLayout gl42, gl21;
    FlowLayout fl;
    GridBagLayout gbl;
    JTextArea accountDispName;
    final JTextArea statusDisplay;
    final JLabel accountDisplayNameHeader;
    final JLabel accountStatusHeader;
    //contructor starts
    AddFormWin() {
        mentionStack = new Stack<String>();
        tagIDList = new ArrayList<String>();
        tweetList = new ArrayList<String>();
        accounts = new ArrayList<Account>();
        f1 = new JFrame("TweetAutomizerVersionBarodaTrial");

        final JTextField timeIntervalInput = new JTextField(5);
        timeIntervalInput.setText("60");
        final JTextField timeIntervalInputMax = new JTextField(5);
        timeIntervalInputMax.setText("180");

        JLabel enterInterval = new JLabel("Enter Minimum Interval In Seconds between two tweets from one account");
        JLabel enterMaxInterval = new JLabel("Enter Maximum Interval In Seconds between two tweets from one account");
        btnAddAcc = new JButton("Add Account");

        gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        final JTextArea tweetLog = new JTextArea(BIG_ROWS, 100);
        JScrollPane tweetLogPaneScroll = new JScrollPane(tweetLog);

        accountDisplayNameHeader = new JLabel("Account");
        accountStatusHeader = new JLabel("Status");
        accountDispName = new JTextArea(BIG_ROWS, 50);
        statusDisplay = new JTextArea(BIG_ROWS, 50);
        statusDisplay.setEditable(false);
        JScrollPane accountDispNameScroll = new JScrollPane(accountDispName);
        JScrollPane statusDispScroll = new JScrollPane(statusDisplay);
        accountDispName.setEditable(false);
        btnAddAcc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
                try {
                    Account account = new Account(APPKEY, APPSECRET);
                    accounts.add(account);
                    int mc = JOptionPane.WARNING_MESSAGE;
                    if (!accountDispName.getText().contains(account.screenName)) {
                        accountDispName.setText(accountDispName.getText() + account.screenName + "\n");
                        accountDispName.setOpaque(true);
                        statusDisplay.setText(statusDisplay.getText() + "active\n");
                        if (!accountDisplayNameHeader.isVisible())
                            accountDisplayNameHeader.setVisible(true);
                        if (!accountStatusHeader.isVisible())
                            accountStatusHeader.setVisible(true);
                        if (!accountDispName.isVisible())
                            accountDispName.setVisible(true);
                        if (!statusDisplay.isVisible())
                            statusDisplay.setVisible(true);
                        //f1.validate();
                        account.persistAccount();
                        JOptionPane.showMessageDialog(null, "Account Added Successfully!!", "Success!!", mc);
                        nAccounts++;
                    } else {
                        JOptionPane.showMessageDialog(null, "Account Already Exists", "Duplicate!!", mc);
                    }
                } catch (Exception e1) {
                    int mc = JOptionPane.WARNING_MESSAGE;
                    JOptionPane.showMessageDialog(null, "Unable To Add Account!", "Error", mc);
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });


        final JTextArea jTextAreaId = new JTextArea(SMALL_ROWS, 50);
        final JTextArea jTextAreaTweets = new JTextArea(SMALL_ROWS, 50);
        jTextAreaId.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
                Color.PINK, Color.GREEN));
        JScrollPane idScrollPane = new JScrollPane(jTextAreaId);
        JScrollPane tweetScrollPane = new JScrollPane(jTextAreaTweets);
        jTextAreaTweets.setEditable(false);
        jTextAreaTweets.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
                Color.PINK, Color.GREEN));


        final JTextField tagIdInsertTextBox = new JTextField(50);
        final JTextField tweetInsertTextBox = new JTextField(50);

        JButton insertId = new JButton("Add Id");
        JButton insertTweet = new JButton("Add Tweet");

        insertId.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
                String text = tagIdInsertTextBox.getText();
                if (text != null && text.length() > 0) {
                    jTextAreaId.setText(jTextAreaId.getText() + "\n" + text);
                    tagIdInsertTextBox.setText("");
                    jTextAreaId.setRows(jTextAreaId.getLineCount());
                    //f1.validate();
                }
            }
        });
        insertTweet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
                String text = tweetInsertTextBox.getText();
                if (text != null && text.length() > 0) {
                    jTextAreaTweets.setText(jTextAreaTweets.getText() + text + "\n");
                    tweetInsertTextBox.setText("");
                    jTextAreaTweets.setRows(jTextAreaTweets.getLineCount());
                    tweetList.add(text);
                    //f1.validate();
                }
            }
        });


        JPanel tweetButtonPanel = new JPanel();
        tweetButtonPanel.setLayout(gbl);
        JButton tweetButton = new JButton("Tweet!!");
        tweetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
                if ((accounts.size() == 0) || (jTextAreaTweets.getText() == null || jTextAreaTweets.getText().length() == 0)) {
                    int mc = JOptionPane.WARNING_MESSAGE;
                    JOptionPane.showMessageDialog(null, "Add an account/tweet/mention to tweet!!", "Attention!!", mc);
                } else {
                    String ids = jTextAreaId.getText();
                    String lines[] = ids.split("\\r?\\n");
                    if (lines.length > 0) {
                        for (String str : lines) {
                            if (str != null && str.length() > 0) {
                                tagIDList.add(str);
                            }
                        }
                    } else {
                    }
                    if (tagIDList.size() > 0) {

                        for (String mention : tagIDList) {
                            for (String tweet : tweetList) {
                                String tempTweet = tweet + " " + mention;
                                mentionStack.push(tempTweet);
                            }
                        }

                    } else {
                        for (String tweet : tweetList) {
                            String tempTweet = tweet;
                            mentionStack.push(tempTweet);
                        }
                    }
                    ArrayList<Thread> threads = new ArrayList<Thread>();
                    long timeIntervalNumber = 1000 * 60;
                    long timeIntervalNumberMax = 1000 *60*3;
                    try {
                        timeIntervalNumber = Long.parseLong(timeIntervalInput.getText()) * 1000;
                        if (timeIntervalNumber < 60000)
                            timeIntervalNumber = 1000 * 60;


                    } catch (Exception e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    try{
                        timeIntervalNumberMax = Long.parseLong(timeIntervalInputMax.getText()) * 1000;
                        if (timeIntervalNumberMax < 60000)
                            timeIntervalNumberMax = 1000 * 60;
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    if(timeIntervalInput==timeIntervalInputMax)
                        timeIntervalNumberMax+=30;
                    for (Account account : accounts) {

                        TweeterThread tweeterThread = new TweeterThread(mentionStack,
                                account, accountDispName,
                                statusDisplay, timeIntervalNumber<timeIntervalNumberMax?timeIntervalNumber:timeIntervalNumberMax,
                                timeIntervalNumber>timeIntervalNumberMax?timeIntervalNumber:timeIntervalNumberMax,
                                tweetLog, f1);
                        Thread t = new Thread(tweeterThread);
                        t.start();
                        nThreads++;
                        threads.add(t);
                    }

                }
            }
        });

        tweetLog.setEditable(false);

        JButton clearAccount = new JButton("Clear Accounts");
        clearAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
                int n = JOptionPane.showConfirmDialog(null,
                        "Are you sure to clear Accounts?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (n == 0) {
                    accountDispName.setText("");
                    statusDisplay.setText("");
                    accountDispName.setVisible(false);
                    statusDisplay.setVisible(false);
                    accounts.clear();
                    nAccounts = 0;
                }
            }
        });
        JButton clearTweetsIdsLogs = new JButton("Clear Tweets,IDS And Logs");
        clearTweetsIdsLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
                int n = JOptionPane.showConfirmDialog(null, "Are you sure to clear the IDS and logs?"
                        , "Confirmation", JOptionPane.YES_NO_OPTION);
                if (n == 0) {
                    tagIdInsertTextBox.setText("");
                    jTextAreaTweets.setText("");
                    tweetInsertTextBox.setText("");
                    jTextAreaId.setText("");
                    tweetLog.setText("");
                    tweetList.clear();
                    tagIDList.clear();
                }
            }
        });

        PromptSupport.setPrompt("Copy Paste Tweets Or Type Here", tweetInsertTextBox);
        PromptSupport.setPrompt("Enter Tweets In Text Box Below.Do Not Try To Type Here", jTextAreaTweets);
        PromptSupport.setPrompt("Enter ID Here And click On Button Below To Add TweetIds", tagIdInsertTextBox);
        PromptSupport.setPrompt("Copy Paste Ids Here Or Enter Below", jTextAreaId);

        tweetLog.setBackground(Color.LIGHT_GRAY);
        f1.setLayout(gbl);
        f1.add(btnAddAcc, makeGbc(0, 0, 1, 1));
        f1.add(clearAccount, makeGbc(0, 1, 1, 1));
        f1.add(accountDisplayNameHeader, makeGbc(1, 0));
        f1.add(accountStatusHeader, makeGbc(1, 1));
        f1.add(accountDispNameScroll, makeGbc(2, 0));
        f1.add(statusDispScroll, makeGbc(2, 1));
        f1.add(enterInterval, makeGbc(3, 0));
        f1.add(timeIntervalInput, makeGbc(3, 1));
        f1.add(enterMaxInterval, makeGbc(4, 0));
        f1.add(timeIntervalInputMax, makeGbc(4, 1));
        f1.add(new JLabel("Twitter Ids"), makeGbc(5, 0));
        f1.add(new JLabel("Tweets"), makeGbc(5, 1));
        f1.add(idScrollPane, makeGbc(6, 0, 5));
        f1.add(tweetScrollPane, makeGbc(6, 1, 5));
        f1.add(tagIdInsertTextBox, makeGbc(11, 0));
        f1.add(tweetInsertTextBox, makeGbc(11, 1));
        f1.add(insertId, makeGbc(12, 0));
        f1.add(insertTweet, makeGbc(12, 1));
        f1.add(tweetButton, makeGbc(13, 0, 1, 1));
        f1.add(clearTweetsIdsLogs, makeGbc(13, 1, 1, 1));
        f1.add(tweetLogPaneScroll, makeGbc(14, 0, 6, 2));
        f1.pack();
        //f1.setSize(800,400);

        f1.setVisible(true);
        f1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        accountDispName.setVisible(false);
        statusDisplay.setVisible(false);
        //firstRun();
    }
    public void firstRun(){
        accounts=Account.getAccountList();
        for(Account account:accounts){
            accountDispName.setText(accountDispName.getText() + account.screenName + "\n");
            accountDispName.setOpaque(true);
            statusDisplay.setText(statusDisplay.getText() + "active\n");
            if (!accountDisplayNameHeader.isVisible())
                accountDisplayNameHeader.setVisible(true);
            if (!accountStatusHeader.isVisible())
                accountStatusHeader.setVisible(true);
            if (!accountDispName.isVisible())
                accountDispName.setVisible(true);
            if (!statusDisplay.isVisible())
                statusDisplay.setVisible(true);
        }

    }
    //Contructor Ends
    private GridBagConstraints makeGbc(int y, int x) {
        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridwidth = 1;
//        gbc.gridheight = 1;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = (y == 0) ? GridBagConstraints.LINE_START : GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.BOTH;
        return gbc;
    }

    private GridBagConstraints makeGbc(int y, int x, int gridheight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridheight = gridheight;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = (y == 0) ? GridBagConstraints.LINE_START : GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.BOTH;
        return gbc;
    }

    private GridBagConstraints makeGbc(int y, int x, int gridheight, int gridwidth) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = (y == 0) ? GridBagConstraints.LINE_START : GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.BOTH;
        return gbc;
    }

    public static void clearTweetStack() {
        mentionStack.clear();
        tweetList.clear();
        tagIDList.clear();
    }

    public static void updateStateOfAccount(JTextArea id, JTextArea stateDisplay, String stateText, Account account) {
        int inde = 0;
        String idArray[] = id.getText().split("\\r?\\n");
        for (String el : idArray) {
            System.out.println("idarray: " + el);
            if (el.compareTo(account.screenName) == 0)
                break;
            else
                inde++;
        }
        String statusArray[] = stateDisplay.getText().split("\\r?\\n");
        System.out.println(statusArray);
        statusArray[inde] = stateText;
        String finalStatusText = "";
        for (String el : statusArray) {
            System.out.println("statusarray: " + statusArray);
            if (!el.endsWith("\n"))
                finalStatusText = finalStatusText + el + "\n";
        }
        //To change body of implemented methods use File | Settings | File Templates.
        stateDisplay.setText(finalStatusText);
    }

}//class ends

//main class starts
class addForm {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AddFormWin a1;
                a1 = new AddFormWin();
            }
        });

    }
}

class TweeterThread implements Runnable {
    private Stack<String> tweets;
    private Account account;
    private JTextArea id;
    private JTextArea status;
    private long sleepInterval;
    private long sleepIntervalMax;
    private JTextArea tweetLog;
    private JFrame frame;

    TweeterThread(Stack<String> tweets, Account account, JTextArea id,
                  JTextArea status, long sleepIntervalMin, long sleepIntervalMax, JTextArea tweetLog, JFrame frame) {
        this.tweets = tweets;
        this.account = account;
        this.id = id;
        this.status = status;
        this.sleepInterval = sleepInterval;
        this.sleepIntervalMax = sleepIntervalMax;
        this.tweetLog = tweetLog;
        this.frame = frame;
    }

    @Override
    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.
        while (!tweets.empty()) {
            synchronized (tweets) {
                synchronized (status) {
                    synchronized (tweetLog) {

                        final String mention = tweets.pop();
                        try {
                            account.updateStatus(mention);
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    //To change body of implemented methods use File | Settings | File Templates.
                                    tweetLog.setText(tweetLog.getText() + tweets.size() + " [" + mention
                                            + "] sent successfully from account @" + account.screenName + "\n");
//                                    frame.validate();
//                                    frame.pack();
                                }
                            });

                        } catch (final TwitterException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            int inde = 0;
                            String idArray[] = id.getText().split("\\r?\\n");
                            for (String el : idArray) {
                                System.out.println("idarray: " + el);
                                if (el.compareTo(account.screenName) == 0)
                                    break;
                                else
                                    inde++;
                            }
                            final int inde2 = inde;

                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {

                                    AddFormWin.updateStateOfAccount(id, status, e.getErrorMessage(), account);
                                    //To change body of implemented methods use File | Settings | File Templates.
                                    tweetLog.setText(tweetLog.getText() + "failed to send tweet '" + mention +
                                            "' From Account " + account.screenName + "due to error:[" + e.getErrorMessage() + "]\n");
                                    tweets.push(mention);
                                }
                            });
                            AddFormWin.nThreads--;
                            //if this was the last thread then show error message and the remaining tweets
                            if (AddFormWin.nThreads == 0 && tweets.size() > 0)
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        //To change body of implemented methods use File | Settings | File Templates.
                                        String finalString = "";
                                        String str;
                                        while (true) {
                                            try {
                                                str = tweets.pop();
                                                finalString += (str + "\n");
                                            } catch (Exception e) {
                                                break;
                                            }
                                        }
                                        AddFormWin.clearTweetStack();
                                        tweetLog.setText(tweetLog.getText() +
                                                "\nFailed to send the following tweets:\n" + finalString);
                                        JOptionPane.showMessageDialog(null, "Tweet Sending Failed!"
                                                , "Failure!!", JOptionPane.WARNING_MESSAGE);

                                    }
                                });
                            return;
                        }

                    }
                }
            }
            try {
                long sleepTime= (long)(sleepInterval + Math.random()*(sleepIntervalMax-sleepInterval));
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        AddFormWin.nThreads--;
        if (tweets.isEmpty())
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    //To change body of implemented methods use File | Settings | File Templates.
                    String finalString = "";
                    String str;
                    while (true) {
                        try {
                            str = tweets.pop();
                            finalString += (str + "\n");
                        } catch (Exception e) {
                            break;
                        }
                    }
                    tweetLog.setText(tweetLog.getText() + "\nFailed to send the following tweets:\n" + finalString);
                    JOptionPane.showMessageDialog(null, "All Tweets Sent Successfully!", "Success!!", JOptionPane.WARNING_MESSAGE);
                }
            });

    }
}
//main class ends
