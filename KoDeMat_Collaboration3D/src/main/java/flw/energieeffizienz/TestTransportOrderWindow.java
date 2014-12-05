/* 
 * Copyright 2014 Institute fml (TU Munich) and Institute FLW (TU Dortmund).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package flw.energieeffizienz;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;
import net.miginfocom.swing.MigLayout;
import org.openide.util.Exceptions;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class TestTransportOrderWindow extends JFrame {

    final VisuClientRealeAnlage visuClientRealeAnlage;
    VisualizationControl visualizationControl = null;
    final MigLayout layout;
    final JPanel panel;
    final JLabel timeLabel;
    final JTextField serverAddressLabel;
    final JLabel clientStateLabel;
    final JLabel currentContainerPos;
    final JTextField setContainerPos;
    final JTextArea logArea;
    final Timer timer = new Timer();
    final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    public void setVisualizationControl(VisualizationControl v) {
        this.visualizationControl = v;
    }

    public TestTransportOrderWindow(String title, final VisuClientRealeAnlage visuClientRealeAnlage) throws HeadlessException {
        super(title);
        this.visuClientRealeAnlage = visuClientRealeAnlage;
        layout = new MigLayout();
        panel = new JPanel(layout);
        serverAddressLabel = new JTextField();
        serverAddressLabel.setText("10.0.2.234:5802");
        serverAddressLabel.setPreferredSize(new Dimension(150, 20));

        clientStateLabel = new JLabel("Not started");
        clientStateLabel.setPreferredSize(new Dimension(150, 20));

        currentContainerPos = new JLabel("");
        setContainerPos = new JTextField("");

        logArea = new JTextArea();
        logArea.setText("");
        DefaultCaret caret = (DefaultCaret) logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        timeLabel = new JLabel();
//        timeLabel.setText(timeFormat.format(sim.currentTime));
        timeLabel.setText("Its time!");

        panel.add(new JLabel("Server:"));
        panel.add(serverAddressLabel, "wrap");

        panel.add(new JButton(new AbstractAction("Start") {
            @Override
            public void actionPerformed(ActionEvent e) {
                visuClientRealeAnlage.start(serverAddressLabel.getText());
            }
        }));

        panel.add(clientStateLabel, "wrap");

        panel.add(new JButton(new AbstractAction("Init") {
            @Override
            public void actionPerformed(ActionEvent e) {
                visuClientRealeAnlage.publishCommand("init");
            }
        }));

        panel.add(new JButton(new AbstractAction("Automove On") {
            @Override
            public void actionPerformed(ActionEvent e) {
                visuClientRealeAnlage.publishCommand("automove on");
            }
        }));
        
        panel.add(new JButton(new AbstractAction("Automove Off") {
            @Override
            public void actionPerformed(ActionEvent e) {
                visuClientRealeAnlage.publishCommand("automove off");
            }
        }));

        panel.add(new JButton(new AbstractAction("Von 1") {
            @Override
            public void actionPerformed(ActionEvent e) {
                visuClientRealeAnlage.publishCommand("von 1");
            }
        }));

        panel.add(new JButton(new AbstractAction("Nach 1") {
            @Override
            public void actionPerformed(ActionEvent e) {
                visuClientRealeAnlage.publishCommand("nach 1");
            }
        }));

        panel.add(new JButton(new AbstractAction("Von 3") {
            @Override
            public void actionPerformed(ActionEvent e) {
                visuClientRealeAnlage.publishCommand("von 3");
            }
        }));

        panel.add(new JButton(new AbstractAction("Nach 3") {
            @Override
            public void actionPerformed(ActionEvent e) {
                visuClientRealeAnlage.publishCommand("nach 3");
            }
        }), "wrap");

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        panel.add(scrollPane, "wrap, grow, push, span 6");

        panel.add(new JButton(new AbstractAction("Clear Log") {
            @Override
            public void actionPerformed(ActionEvent e) {
                logArea.setText("");
            }
        }), "wrap");


        panel.add(new JLabel("Simulation Options"), "wrap");
        panel.add(new JLabel("Current Container Pos:"));
        panel.add(currentContainerPos, "wrap, growx, pushx");

        panel.add(new JLabel("Set Container Position:"));
        panel.add(setContainerPos, "growx, pushx");

        panel.add(new JButton(new AbstractAction("Set") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (visualizationControl != null) {
                    visualizationControl.setPositionEEAFeldhorst(setContainerPos.getText());
                }
            }
        }));
        panel.add(new JButton(new AbstractAction("Platz 1") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (visualizationControl != null) {
                    visualizationControl.setPositionEEAFeldhorst("c97");
                }
            }
        }));
        panel.add(new JButton(new AbstractAction("Platz 3") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (visualizationControl != null) {
                    visualizationControl.setPositionEEAFeldhorst("c100");
                }
            }
        }));
        
        panel.add(new JButton(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (visualizationControl != null) {
                    visualizationControl.deleteContainer();
                }
            }
        }), "wrap");

        panel.add(new JButton(new AbstractAction("Start Sim 1") {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulateEventsFromFile("./flw_eea_test_data_1.txt");
            }
        }));

        panel.add(new JButton(new AbstractAction("Start Sim 2") {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulateEventsFromFile("./flw_eea_test_data_2.txt");
            }
        }), "wrap");

//        panel.add(new JLabel("Current Simulation Time:"));
//        panel.add(timeLabel, "wrap");

        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        };
        this.addWindowListener(exitListener);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }

    public void close() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                visuClientRealeAnlage.shutdown();
                timer.cancel();
                setVisible(false);
                dispose();
            }
        });
    }

    public void addLogMessage(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                logArea.append(timeFormat.format(System.currentTimeMillis()) + " " + msg);
                logArea.append("\n");
            }
        });
    }

    public void setClientState(final String state) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                clientStateLabel.setText(state);
            }
        });
    }

    public void setContainerFeldhorstPos(final String pos) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentContainerPos.setText(pos);
            }
        });
    }

    public static void main(String[] args) {
        VisuClientRealeAnlage c = new VisuClientRealeAnlage();
        TestTransportOrderWindow win = new TestTransportOrderWindow("Test Transport Order", c);
        c.setWindow(win);
        win.start();
    }

    private void simulateEventsFromFile(String path) {
        Path file = Paths.get(path);
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

            Long offsetTime = null;

            for (String string : lines) {
                int spacepos = string.indexOf(" ");


                long texttime = Long.parseLong(string.substring(0, spacepos));
                if (offsetTime == null) {
                    offsetTime = texttime;
                }
                texttime -= offsetTime;

                final String msg = string.substring(spacepos, string.length());

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        visuClientRealeAnlage.publishEvent(msg);
                    }
                }, texttime);




            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
