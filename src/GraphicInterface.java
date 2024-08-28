import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicInterface extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    public List<Regular> regular;
    public List<Contributor> contributor;
    public List<Admin> admin;
    public IMDB imdb;
    private ImageIcon backgroundImage;
    public GraphicInterface(List<Regular> regular, List<Contributor> contributor, List<Admin> admin, IMDB imdb) {

        this.regular = regular;
        this.contributor = contributor;
        this.admin = admin;

        this.imdb = imdb;

        setTitle("Login");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        backgroundImage = new ImageIcon("images/p1.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setSize(1000, 600);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(700, 200, 95, 30);
        emailField = new JTextField(15);
        emailField.setBounds(700, 230, 180, 30);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(700, 260, 95, 30);
        passwordField = new JPasswordField(15);
        passwordField.setBounds(700, 290, 180, 30);
        loginButton = new JButton("Login");
        loginButton.setBounds(700, 350, 95, 30);

        backgroundLabel.add(emailLabel);
        backgroundLabel.add(emailField);
        backgroundLabel.add(passwordLabel);
        backgroundLabel.add(passwordField);
        backgroundLabel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                boolean isLoggedIn = Autentification.autentification(email, password, regular, contributor, admin);

                if (isLoggedIn) {
                    Regular regUsr = null;
                    Contributor contrUsr = null;
                    Admin admUsr = null;

                    for (Regular r : imdb.regular) {
                        Credentials credentials = r.info.getCredentials();
                        if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                            regUsr = r;
                            break;
                        }
                    }
                    for (Contributor c : imdb.contributor) {
                        Credentials credentials = c.info.getCredentials();
                        if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                            contrUsr = c;
                            break;
                        }
                    }
                    for (Admin a : imdb.admin) {
                        Credentials credentials = a.info.getCredentials();
                        if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                            admUsr = a;
                            break;
                        }
                    }

                    if (regUsr != null) {
                        RegUserInterface reg = new RegUserInterface(imdb, regUsr.username);
                        reg.setVisible(true);
                        dispose();
//                        RegUserInterface reg = new RegUserInterface(imdb, regUsr.username);
//                        setContentPane(reg.getContentPane());
//                        revalidate();
//                        repaint();
                    }
                    if (contrUsr != null) {
                        ContrUserInterface contr = new ContrUserInterface(imdb, contrUsr.username);
                        contr.setVisible(true);
                        dispose();
                    }
                    if (admUsr != null) {
                        AdminUserInterface adm = new AdminUserInterface(imdb, admUsr.username);
                        adm.setVisible(true);
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect credentials!");
                }
            }
        });
        //setPreferredSize(new Dimension(1000, 600));

        add(backgroundLabel);
        setVisible(true);
    }
}

class RegUserInterface extends JFrame {
    public IMDB imdb;
    String username;
    public JButton actorsbutton;
    public JButton productionsbutton;
    public JButton favorites;
    public JButton Rqr;
    public JButton viewNotifications;
    public JButton logout;
    public RegUserInterface(IMDB imdb, String username) {
        this.imdb = imdb;
        this.username = username;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        productionsbutton = new JButton();
        ImageIcon icon = new ImageIcon("images/p2.gif");
        productionsbutton.setIcon(icon);

        actorsbutton = new JButton();
        ImageIcon icon2 = new ImageIcon("images/p3.gif");
        actorsbutton.setIcon(icon2);

        favorites = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p4.gif");
        favorites.setIcon(icon3);

        Rqr = new JButton();
        ImageIcon icon4 = new ImageIcon("images/p5.gif");
        Rqr.setIcon(icon4);

        viewNotifications = new JButton();
        ImageIcon icon7 = new ImageIcon("images/p15.gif");
        viewNotifications.setIcon(icon7);

        logout = new JButton("Logout");
        ImageIcon icon5 = new ImageIcon("images/p6.gif");
        logout.setIcon(icon5);

        panel.add(Box.createRigidArea(new Dimension(250, 0)));
        panel.add(new JLabel("Welcome back!"));
        panel.add(new JLabel("\n"));
        panel.add(new JLabel("User: " + this.username));
        panel.add(new JLabel("\n"));
        for (Regular r : imdb.regular) {
            if (r.username.equals(username)) {
                double exp = r.getExperience();
                String expstr = String.valueOf(exp);
                JLabel label = new JLabel("Experience: " + expstr);
                panel.add(label);
                break;
            }
        }
        panel.add(new JLabel("\n"));
        panel.add(productionsbutton);
        panel.add(actorsbutton);
        panel.add(favorites);
        panel.add(Rqr);
        panel.add(viewNotifications);
        panel.add(logout);
        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);

        productionsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductionsInterface productionsInterface = new ProductionsInterface(imdb, username, "regular");
                productionsInterface.setVisible(true);
                dispose();
            }
        });

        actorsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActorsInterface actorsInterface = new ActorsInterface(imdb, username, "regular");
                actorsInterface.setVisible(true);
                dispose();
            }
        });

        favorites.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FavoritesInterface favoritesInterface = new FavoritesInterface(imdb, username, "regular");
                favoritesInterface.setVisible(true);
                dispose();
            }
        });

        Rqr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RqrInterface rqrInterface = new RqrInterface(imdb, username, "regular");
                rqrInterface.setVisible(true);
                dispose();
            }
        });

        viewNotifications.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewNotificationsInterface viewNotificationsInterface = new ViewNotificationsInterface(imdb, username, "regular");
                viewNotificationsInterface.setVisible(true);
                dispose();
            }
        });

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphicInterface graphicInterface = new GraphicInterface(imdb.regular, imdb.contributor, imdb.admin, imdb);
                graphicInterface.setVisible(true);
                dispose();
            }
        });
    }
}

class ContrUserInterface extends JFrame {
    public IMDB imdb;
    String username;
    public JButton actorsbutton;
    public JButton productionsbutton;
    public JButton favorites;
    public JButton Rqr;
    public JButton viewRequests;
    public JButton addProduction;
    public JButton viewNotifications;
    public JButton updateProduction;
    public JButton updateActor;
    public JButton logout;
    public ContrUserInterface(IMDB imdb, String username) {
        this.imdb = imdb;
        this.username = username;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        productionsbutton = new JButton();
        ImageIcon icon = new ImageIcon("images/p2.gif");
        productionsbutton.setIcon(icon);

        actorsbutton = new JButton();
        ImageIcon icon2 = new ImageIcon("images/p3.gif");
        actorsbutton.setIcon(icon2);

        favorites = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p4.gif");
        favorites.setIcon(icon3);

        Rqr = new JButton();
        ImageIcon icon4 = new ImageIcon("images/p5.gif");
        Rqr.setIcon(icon4);

        viewRequests = new JButton();
        ImageIcon icon6 = new ImageIcon("images/p14.gif");
        viewRequests.setIcon(icon6);

        addProduction = new JButton();
        ImageIcon icon8 = new ImageIcon("images/p16.gif");
        addProduction.setIcon(icon8);

        viewNotifications = new JButton();
        ImageIcon icon7 = new ImageIcon("images/p15.gif");
        viewNotifications.setIcon(icon7);

        updateProduction = new JButton();
        ImageIcon icon9 = new ImageIcon("images/p17.gif");
        updateProduction.setIcon(icon9);

        updateActor = new JButton();
        ImageIcon icon10 = new ImageIcon("images/p17.gif");
        updateActor.setIcon(icon10);

        logout = new JButton("Logout");
        ImageIcon icon5 = new ImageIcon("images/p6.gif");
        logout.setIcon(icon5);

        panel.add(Box.createRigidArea(new Dimension(250, 0)));
        panel.add(new JLabel("Welcome back!"));
        panel.add(new JLabel("\n"));
        panel.add(new JLabel("User: " + this.username));
        panel.add(new JLabel("\n"));
        for (Regular r : imdb.regular) {
            if (r.username.equals(username)) {
                double exp = r.getExperience();
                String expstr = String.valueOf(exp);
                JLabel label = new JLabel("Experience: " + expstr);
                panel.add(label);
                break;
            }
        }
        panel.add(new JLabel("\n"));
        panel.add(productionsbutton);
        panel.add(actorsbutton);
        panel.add(favorites);
        panel.add(Rqr);
        panel.add(viewRequests);
        panel.add(viewNotifications);
        panel.add(new JLabel("Add/Remove production:"));
        panel.add(addProduction);
        panel.add(new JLabel("Update production:"));
        panel.add(updateProduction);
        panel.add(new JLabel("Update actor:"));
        panel.add(updateActor);
        panel.add(logout);
        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);

        productionsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductionsInterface productionsInterface = new ProductionsInterface(imdb, username, "contributor");
                productionsInterface.setVisible(true);
                dispose();
            }
        });

        actorsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActorsInterface actorsInterface = new ActorsInterface(imdb, username, "contributor");
                actorsInterface.setVisible(true);
                dispose();
            }
        });

        favorites.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FavoritesInterface favoritesInterface = new FavoritesInterface(imdb, username, "contributor");
                favoritesInterface.setVisible(true);
                dispose();
            }
        });

        Rqr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RqrInterface rqrInterface = new RqrInterface(imdb, username, "contributor");
                rqrInterface.setVisible(true);
                dispose();
            }
        });

        viewNotifications.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewNotificationsInterface viewNotificationsInterface = new ViewNotificationsInterface(imdb, username, "contributor");
                viewNotificationsInterface.setVisible(true);
                dispose();
            }
        });

        viewRequests.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewRequestsInterface viewRequestsInterface = new ViewRequestsInterface(imdb, username, "contributor");
                viewRequestsInterface.setVisible(true);
                dispose();
            }
        });

        addProduction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddProductionInterface addProductionInterface = new AddProductionInterface(imdb, username, "contributor");
                addProductionInterface.setVisible(true);
                dispose();
            }
        });

        updateProduction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateProductionInterface updateProductionInterface = new UpdateProductionInterface(imdb, username, "contributor");
                updateProductionInterface.setVisible(true);
            }
        });

        updateActor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateActorInterface updateActorInterface = new UpdateActorInterface(imdb, username, "contributor");
                updateActorInterface.setVisible(true);
            }
        });

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphicInterface graphicInterface = new GraphicInterface(imdb.regular, imdb.contributor, imdb.admin, imdb);
                graphicInterface.setVisible(true);
                dispose();
            }
        });
    }
}

class AdminUserInterface extends JFrame {
    public IMDB imdb;
    String username;
    public JButton actorsbutton;
    public JButton productionsbutton;
    public JButton favorites;
    public JButton viewRequests;
    public JButton addProduction;
    public JButton viewNotifications;
    public JButton updateProduction;
    public JButton updateActor;
    public JButton removeuser;
    public JButton logout;
    public AdminUserInterface(IMDB imdb, String username) {
        this.imdb = imdb;
        this.username = username;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        productionsbutton = new JButton();
        ImageIcon icon = new ImageIcon("images/p2.gif");
        productionsbutton.setIcon(icon);

        actorsbutton = new JButton();
        ImageIcon icon2 = new ImageIcon("images/p3.gif");
        actorsbutton.setIcon(icon2);

        favorites = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p4.gif");
        favorites.setIcon(icon3);

        viewRequests = new JButton();
        ImageIcon icon6 = new ImageIcon("images/p14.gif");
        viewRequests.setIcon(icon6);

        addProduction = new JButton();
        ImageIcon icon8 = new ImageIcon("images/p16.gif");
        addProduction.setIcon(icon8);

        viewNotifications = new JButton();
        ImageIcon icon7 = new ImageIcon("images/p15.gif");
        viewNotifications.setIcon(icon7);

        updateProduction = new JButton();
        ImageIcon icon9 = new ImageIcon("images/p17.gif");
        updateProduction.setIcon(icon9);

        updateActor = new JButton();
        ImageIcon icon10 = new ImageIcon("images/p17.gif");
        updateActor.setIcon(icon10);

        removeuser = new JButton();
        ImageIcon icon11 = new ImageIcon("images/p18.gif");
        removeuser.setIcon(icon11);

        logout = new JButton("Logout");
        ImageIcon icon5 = new ImageIcon("images/p6.gif");
        logout.setIcon(icon5);

        panel.add(Box.createRigidArea(new Dimension(250, 0)));
        panel.add(new JLabel("Welcome back!"));
        panel.add(new JLabel("\n"));
        panel.add(new JLabel("User: " + this.username));
        panel.add(new JLabel("\n"));
        for (Regular r : imdb.regular) {
            if (r.username.equals(username)) {
                double exp = r.getExperience();
                String expstr = String.valueOf(exp);
                JLabel label = new JLabel("Experience: " + expstr);
                panel.add(label);
                break;
            }
        }
        panel.add(new JLabel("\n"));
        panel.add(productionsbutton);
        panel.add(actorsbutton);
        panel.add(favorites);
        panel.add(viewRequests);
        panel.add(viewNotifications);
        panel.add(new JLabel("Add/Remove production:"));
        panel.add(addProduction);
        panel.add(new JLabel("Update production:"));
        panel.add(updateProduction);
        panel.add(new JLabel("Update actor:"));
        panel.add(updateActor);
        panel.add(removeuser);
        panel.add(logout);
        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);

        productionsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductionsInterface productionsInterface = new ProductionsInterface(imdb, username, "admin");
                productionsInterface.setVisible(true);
                dispose();
            }
        });

        actorsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActorsInterface actorsInterface = new ActorsInterface(imdb, username, "admin");
                actorsInterface.setVisible(true);
                dispose();
            }
        });

        favorites.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FavoritesInterface favoritesInterface = new FavoritesInterface(imdb, username, "admin");
                favoritesInterface.setVisible(true);
                dispose();
            }
        });

        viewNotifications.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewNotificationsInterface viewNotificationsInterface = new ViewNotificationsInterface(imdb, username, "admin");
                viewNotificationsInterface.setVisible(true);
                dispose();
            }
        });

        viewRequests.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewRequestsAdmInterface viewRequestsInterfaceadm = new ViewRequestsAdmInterface(imdb, username, "admin");
                viewRequestsInterfaceadm.setVisible(true);
                dispose();
            }
        });

        addProduction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddProductionInterface addProductionInterface = new AddProductionInterface(imdb, username, "admin");
                addProductionInterface.setVisible(true);
                dispose();
            }
        });

        updateProduction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateProductionInterface updateProductionInterface = new UpdateProductionInterface(imdb, username, "admin");
                updateProductionInterface.setVisible(true);
            }
        });

        updateActor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateActorInterface updateActorInterface = new UpdateActorInterface(imdb, username, "admin");
                updateActorInterface.setVisible(true);
            }
        });

        removeuser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RemoveUserInterface removeUserInterface = new RemoveUserInterface(imdb, username, "admin");
                removeUserInterface.setVisible(true);
            }
        });

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphicInterface graphicInterface = new GraphicInterface(imdb.regular, imdb.contributor, imdb.admin, imdb);
                graphicInterface.setVisible(true);
                dispose();
            }
        });
    }
}


class ProductionsInterface extends JFrame {
    public IMDB imdb;
    String username;
    public JButton movies;
    public JButton series;
    public JButton back;
    String type;
    public ProductionsInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        movies = new JButton();
        ImageIcon icon = new ImageIcon("images/p7.gif");
        movies.setIcon(icon);

        series = new JButton();
        ImageIcon icon2 = new ImageIcon("images/p8.gif");
        series.setIcon(icon2);

        back = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p9.gif");
        back.setIcon(icon3);

        panel.add(Box.createRigidArea(new Dimension(250, 0)));
        panel.add(new JLabel("Choose a category:"));
        panel.add(new JLabel("\n"));
        panel.add(movies);
        panel.add(series);
        panel.add(back);
        add(panel);

        movies.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MoviesInterface moviesInterface = new MoviesInterface(imdb, username, type);
                moviesInterface.setVisible(true);
                dispose();
            }
        });

        series.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SeriesInterface seriesInterface = new SeriesInterface(imdb, username, type);
                seriesInterface.setVisible(true);
                dispose();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type.equals("regular")) {
                    RegUserInterface reg = new RegUserInterface(imdb, username);
                    reg.setVisible(true);
                    dispose();
                } else if (type.equals("contributor")) {
                    ContrUserInterface contr = new ContrUserInterface(imdb, username);
                    contr.setVisible(true);
                    dispose();
                } else {
                    AdminUserInterface adm = new AdminUserInterface(imdb, username);
                    adm.setVisible(true);
                    dispose();
                }
            }
        });
    }
}

class ActorsInterface extends JFrame {
    public IMDB imdb;
    public String username;
    String type;
    public JButton rate;
    public JButton back;
    public JButton search;
    public JButton searchAfterRate;

    public ActorsInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        rate = new JButton();
        ImageIcon icon = new ImageIcon("images/p10.gif");
        rate.setIcon(icon);

        back = new JButton();
        ImageIcon icon2 = new ImageIcon("images/p9.gif");
        back.setIcon(icon2);

        search = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p12.gif");
        search.setIcon(icon3);

        searchAfterRate = new JButton("");
        ImageIcon icon6 = new ImageIcon("images/p12.gif");
        searchAfterRate.setIcon(icon6);

        panel.add(Box.createRigidArea(new Dimension(250, 0)));
        panel.add(new JLabel("Actors"));
        panel.add(new JLabel("\n"));
        panel.add(new JLabel("Search actor"));
        JTextField textField = new JTextField(10);
        panel.add(textField);
        panel.add(search);

        Integer nr = 1;
        for (Actor a : imdb.actor) {
            JLabel number = new JLabel(nr.toString() + ") ");
            nr = nr + 1;
            JLabel name = new JLabel("Name: " + a.getName());
            JLabel bio = new JLabel("Biography: " + a.getBiography());
            double ratng = a.getTotalRating();
            String rating = String.valueOf(ratng);
            JLabel rating2 = new JLabel("Rating: " + rating);

            JPanel p = new JPanel();
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
            p.add(number);
            p.add(name);
            if (a.biography != null) {
                p.add(bio);
            }
            p.add(rating2);
            if (a.roles.keySet() != null) {
                p.add(new JLabel("Performances: "));
                for (String key : a.roles.keySet()) {
                    p.add(new JLabel(key + " - " + a.roles.get(key)));
                }
            }
            panel.add(p);

        }
        panel.add(new JLabel("\n"));
        panel.add(new JLabel("Search actors after rating"));
        JTextField textField2 = new JTextField(10);
        panel.add(textField2);
        panel.add(searchAfterRate);
        panel.add(rate);
        panel.add(back);
        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane);

        rate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddRateActorsInterface addRateInterface = new AddRateActorsInterface(imdb, username, type);
                addRateInterface.setVisible(true);
                dispose();
            }
        });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                boolean ok = false;
                for (Actor a : imdb.actor) {
                    if (a.name.equals(s)) {
                        SearchActor searchactor = new SearchActor(a);
                        searchactor.setVisible(true);
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    JOptionPane.showMessageDialog(null, "Actor not found!");
                }
            }
        });

        searchAfterRate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField2.getText();
                if (Double.parseDouble(s) < 1 || Double.parseDouble(s) > 10) {
                    JOptionPane.showMessageDialog(null, "Invalid rating!");
                    return;
                }
                boolean ok = false;
                for (Actor act : imdb.actor) {
                    if (act.TotalRating >= Double.parseDouble(s)) {
                        SearchMovieSerialActorRating searchactorsRating = new SearchMovieSerialActorRating(imdb, Double.parseDouble(s), "actors");
                        searchactorsRating.setVisible(true);
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    JOptionPane.showMessageDialog(null, "No actors were found!");
                }
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type.equals("regular")) {
                    RegUserInterface reg = new RegUserInterface(imdb, username);
                    reg.setVisible(true);
                    dispose();
                } else if (type.equals("contributor")) {
                    ContrUserInterface contr = new ContrUserInterface(imdb, username);
                    contr.setVisible(true);
                    dispose();
                } else {
                    AdminUserInterface adm = new AdminUserInterface(imdb, username);
                    adm.setVisible(true);
                    dispose();
                }
            }
        });

    }
}

class FavoritesInterface extends JFrame {
    public IMDB imdb;
    String username;
    String type;
    public JButton back;
    public JButton addFav;
    public JButton rmvFavorites;

    public FavoritesInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        back = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p9.gif");
        back.setIcon(icon3);

        addFav = new JButton();
        ImageIcon icon4 = new ImageIcon("images/p11.gif");
        addFav.setIcon(icon4);

        rmvFavorites = new JButton();
        ImageIcon icon5 = new ImageIcon("images/p13.gif");
        rmvFavorites.setIcon(icon5);

        panel.add(Box.createRigidArea(new Dimension(250, 0)));
        panel.add(new JLabel("Favorite list:"));
        panel.add(new JLabel("\n"));

        boolean ok = false;
        for (Regular r : imdb.regular) {
            if (r.username.equals(username)) {
                ok = true;
                Integer nr = 1;
                for (Object p : r.favorites) {
                    panel.add(new JLabel(nr.toString() + ") "));
                    JLabel title = new JLabel(p.toString());
                    panel.add(title);
                    nr = nr + 1;
                }
                break;
            }
        }
        if (!ok) {
            for (Contributor c : imdb.contributor) {
                if (c.username.equals(username)) {
                    ok = true;
                    Integer nr = 1;
                    for (Object p : c.favorites) {
                        panel.add(new JLabel(nr.toString() + ") "));
                        JLabel title = new JLabel(p.toString());
                        panel.add(title);
                        nr = nr + 1;
                    }
                    break;
                }
            }
        }
        if (!ok) {
            for (Admin a : imdb.admin) {
                if (a.username.equals(username)) {
                    ok = true;
                    Integer nr = 1;
                    for (Object p : a.favorites) {
                        panel.add(new JLabel(nr.toString() + ") "));
                        JLabel title = new JLabel(p.toString());
                        panel.add(title);
                        nr = nr + 1;
                    }
                    break;
                }
            }
        }

        panel.add(addFav);
        panel.add(rmvFavorites);
        panel.add(back);
        add(panel);

        addFav.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddFavoritesInterface addFavoritesInterface = new AddFavoritesInterface(imdb, username);
                addFavoritesInterface.setVisible(true);
            }
        });

        rmvFavorites.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RemoveFavoritesInterface removeFavoritesInterface = new RemoveFavoritesInterface(imdb, username);
                removeFavoritesInterface.setVisible(true);
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type.equals("regular")) {
                    RegUserInterface reg = new RegUserInterface(imdb, username);
                    reg.setVisible(true);
                    dispose();
                } else if (type.equals("contributor")) {
                    ContrUserInterface contr = new ContrUserInterface(imdb, username);
                    contr.setVisible(true);
                    dispose();
                } else {
                    AdminUserInterface adm = new AdminUserInterface(imdb, username);
                    adm.setVisible(true);
                    dispose();
                }
            }
        });
    }
}

class RqrInterface extends JFrame {
    public IMDB imdb;
    public String username;
    String type;
    public JButton add;
    public JButton back;

    public RqrInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        add = new JButton("");
        ImageIcon icon2 = new ImageIcon("images/p11.gif");
        add.setIcon(icon2);

        back = new JButton("");
        ImageIcon icon3 = new ImageIcon("images/p9.gif");
        back.setIcon(icon3);

        panel.add(Box.createRigidArea(new Dimension(250, 0)));
        panel.add(new JLabel("Choose the request type number:"));
        panel.add(new JLabel("1) Actor issue"));
        panel.add(new JLabel("2) Movie issue"));
        panel.add(new JLabel("3) Delete account"));
        panel.add(new JLabel("4) Other"));
        JTextField textField = new JTextField(10);
        panel.add(textField);
        panel.add(new JLabel("Type the name of the request:"));
        JTextField textField2 = new JTextField(10);
        panel.add(textField2);
        panel.add(new JLabel("Type the description of the request:"));
        JTextField textField3 = new JTextField(10);
        panel.add(textField3);
        panel.add(add);
        panel.add(back);
        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                String name = textField2.getText();
                String description = textField3.getText();
                if (s.equals("1")) {
                    for (Contributor c : imdb.contributor) {
                        for (Object o : c.prod) {
                            if (o instanceof Actor) {
                                Actor a = (Actor) o;
                                if (a.name.equals(name)) {
                                    Request request = new Request(RequestTypes.ACTOR_ISSUE, name, description, username, c.username, imdb);
                                    c.r.add(request);
                                    JOptionPane.showMessageDialog(null, "Request sent!");
                                    return;
                                }
                            }
                        }
                    }
                    for (Admin adm : imdb.admin) {
                        for (Object o : adm.prod) {
                            if (o instanceof Actor) {
                                Actor a = (Actor) o;
                                if (a.name.equals(name)) {
                                    Request request = new Request(RequestTypes.ACTOR_ISSUE, name, description, username, adm.username, imdb);
                                    adm.r.add(request);
                                    JOptionPane.showMessageDialog(null, "Request sent!");
                                    return;
                                }
                            }
                        }
                    }
                    //Request request = new Request(RequestTypes.ACTOR_ISSUE, name, description, username, imdb);
                    JOptionPane.showMessageDialog(null, "Request sent!");
                } else if (s.equals("2")) {
                    for (Contributor c : imdb.contributor) {
                        for (Object o : c.prod) {
                            if (o instanceof Movie) {
                                Movie m = (Movie) o;
                                if (m.getTitle().equals(name)) {
                                    Request request = new Request(RequestTypes.MOVIE_ISSUE, name, description, username, c.username, imdb);
                                    c.r.add(request);
                                    JOptionPane.showMessageDialog(null, "Request sent!");
                                    return;
                                }
                            }
                            if (o instanceof Series) {
                                Series ser = (Series) o;
                                if (ser.getTitle().equals(name)) {
                                    Request request = new Request(RequestTypes.MOVIE_ISSUE, name, description, username, c.username, imdb);
                                    c.r.add(request);
                                    JOptionPane.showMessageDialog(null, "Request sent!");
                                    return;
                                }
                            }
                        }
                    }
                    for (Admin adm : imdb.admin) {
                        for (Object o : adm.prod) {
                            if (o instanceof Movie) {
                                Movie m = (Movie) o;
                                if (m.getTitle().equals(name)) {
                                    Request request = new Request(RequestTypes.MOVIE_ISSUE, name, description, username, adm.username, imdb);
                                    adm.r.add(request);
                                    JOptionPane.showMessageDialog(null, "Request sent!");
                                    return;
                                }
                            }
                            if (o instanceof Series) {
                                Series ser = (Series) o;
                                if (ser.getTitle().equals(name)) {
                                    Request request = new Request(RequestTypes.MOVIE_ISSUE, name, description, username, adm.username, imdb);
                                    adm.r.add(request);
                                    JOptionPane.showMessageDialog(null, "Request sent!");
                                    return;
                                }
                            }
                        }
                    }
                } else if (s.equals("3")) {
                    Request request = new Request(RequestTypes.DELETE_ACCOUNT, name, description, username, "ADMIN", imdb);
                    RequestsHolder.addRequest(request);
                    JOptionPane.showMessageDialog(null, "Request sent!");
                } else if (s.equals("4")) {
                    Request request = new Request(RequestTypes.OTHERS, name, description, username, "ADMIN", imdb);
                    RequestsHolder.addRequest(request);
                    JOptionPane.showMessageDialog(null, "Request sent!");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid request type!");
                }
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type.equals("regular")) {
                    RegUserInterface reg = new RegUserInterface(imdb, username);
                    reg.setVisible(true);
                    dispose();
                } else if (type.equals("contributor")) {
                    ContrUserInterface contr = new ContrUserInterface(imdb, username);
                    contr.setVisible(true);
                    dispose();
                } else {
                    AdminUserInterface adm = new AdminUserInterface(imdb, username);
                    adm.setVisible(true);
                    dispose();
                }
            }
        });
    }
}

class MoviesInterface extends JFrame {
    public IMDB imdb;
    public String username;
    String type;
    public JButton back;

    public JButton addRate;
    public JButton search;
    public JButton searchAfterRate;
    public JButton searchAfterGenre;
    public MoviesInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        back = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p9.gif");
        back.setIcon(icon3);

        addRate = new JButton("");
        ImageIcon icon4 = new ImageIcon("images/p10.gif");
        addRate.setIcon(icon4);

        search = new JButton("");
        ImageIcon icon5 = new ImageIcon("images/p12.gif");
        search.setIcon(icon5);

        searchAfterRate = new JButton("");
        ImageIcon icon6 = new ImageIcon("images/p12.gif");
        searchAfterRate.setIcon(icon6);

        searchAfterGenre = new JButton("");
        ImageIcon icon7 = new ImageIcon("images/p12.gif");
        searchAfterGenre.setIcon(icon7);

        panel.add(Box.createRigidArea(new Dimension(250, 0)));
        panel.add(new JLabel("Movies"));
        panel.add(new JLabel("\n"));
        panel.add(new JLabel("Search movie"));
        JTextField textField = new JTextField(10);
        panel.add(textField);
        panel.add(search);

        Integer nr = 1;
        for (Movie m : imdb.movie) {
            JLabel number = new JLabel(nr.toString() + ") ");
            nr = nr + 1;
            JLabel title = new JLabel("Title: " + m.getTitle());
            JLabel year = new JLabel("Release Year: " + m.getYear_of_Release().toString());
            JLabel rating = new JLabel("Rating: " + m.getTotalRating().toString());
            JLabel genres = new JLabel("Genres: " + m.getGenres().toString());
            JLabel duration = new JLabel("Duration: " + m.getDuration().toString());
            JLabel actors = new JLabel("Actors: " + m.getActors().toString());
            JLabel directors = new JLabel("Directors: " + m.getDirectors().toString());
            JLabel description = new JLabel("Description: " + m.getDescription());
            JPanel p = new JPanel();
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
            p.add(number);
            p.add(title);
            p.add(year);
            p.add(rating);
            p.add(genres);
            p.add(duration);
            p.add(actors);
            p.add(directors);
            if (m.Description != null) {
                p.add(description);
            }
            panel.add(p);
        }
        panel.add(new JLabel("\n"));
        panel.add(new JLabel("Search movies after rating"));
        JTextField textField2 = new JTextField(10);
        panel.add(textField2);
        panel.add(searchAfterRate);
        panel.add(new JLabel("Search movie after genre"));
        JTextField textField3 = new JTextField(10);
        panel.add(textField3);
        panel.add(searchAfterGenre);

        panel.add(addRate);
        panel.add(back);
        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                boolean ok = false;
                for (Movie m : imdb.movie) {
                    if (m.title.equals(s)) {
                        SearchMovie searchmovie = new SearchMovie(m);
                        searchmovie.setVisible(true);
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    JOptionPane.showMessageDialog(null, "Movie not found!");
                }
            }
        });

        searchAfterRate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField2.getText();
                if (Double.parseDouble(s) < 1 || Double.parseDouble(s) > 10) {
                    JOptionPane.showMessageDialog(null, "Invalid rating!");
                    return;
                }
                boolean ok = false;
                for (Movie m : imdb.movie) {
                    if (m.TotalRating >= Double.parseDouble(s)) {
                        SearchMovieSerialActorRating searchmovieRating = new SearchMovieSerialActorRating(imdb, Double.parseDouble(s), "movie");
                        searchmovieRating.setVisible(true);
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    JOptionPane.showMessageDialog(null, "No movies were found!");
                }
            }
        });

        searchAfterGenre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField3.getText();
                SearchMovieSerialGenre searchmovieGenre = new SearchMovieSerialGenre(imdb, "movie", s);
                searchmovieGenre.setVisible(true);
            }
        });

        addRate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddRateMovieInterface addRateInterface = new AddRateMovieInterface(imdb, username, type);
                addRateInterface.setVisible(true);
                dispose();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductionsInterface productionsInterface = new ProductionsInterface(imdb, username, type);
                productionsInterface.setVisible(true);
                dispose();
            }
        });
    }
}

class SeriesInterface extends JFrame{
    public IMDB imdb;
    public String username;
    String type;
    public JButton back;
    public JButton search;
    public JButton addRate;
    public JButton searchAfterRate;
    public JButton searchAfterGenre;

    public SeriesInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        back = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p9.gif");
        back.setIcon(icon3);

        addRate = new JButton("");
        ImageIcon icon4 = new ImageIcon("images/p10.gif");
        addRate.setIcon(icon4);

        search = new JButton("");
        ImageIcon icon5 = new ImageIcon("images/p12.gif");
        search.setIcon(icon5);

        searchAfterRate = new JButton("");
        ImageIcon icon6 = new ImageIcon("images/p12.gif");
        searchAfterRate.setIcon(icon6);

        searchAfterGenre = new JButton("");
        ImageIcon icon7 = new ImageIcon("images/p12.gif");
        searchAfterGenre.setIcon(icon7);

        panel.add(Box.createRigidArea(new Dimension(250, 0)));
        panel.add(new JLabel("Movies"));
        panel.add(new JLabel("\n"));
        panel.add(new JLabel("Search series"));
        JTextField textField = new JTextField(10);
        panel.add(textField);
        panel.add(search);

        Integer nr = 1;
        for (Series s : imdb.series) {
            JLabel number = new JLabel(nr.toString() + ") ");
            nr = nr + 1;
            JLabel title = new JLabel("Title: " + s.getTitle());
            JLabel year = new JLabel("Release Year: " + s.getYear().toString());
            JLabel rating = new JLabel("Rating: " + s.getTotalRating().toString());
            JLabel genres = new JLabel("Genres: " + s.getGenres().toString());
            JLabel actors = new JLabel("Actors: " + s.getActors().toString());
            JLabel directors = new JLabel("Directors: " + s.getDirectors().toString());
            JLabel description = new JLabel("Description: " + s.getDescription());
            JLabel seasons = new JLabel("Number of seasons: " + s.getNumberOfSeasons().toString());

            JPanel p = new JPanel();
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
            p.add(number);
            p.add(title);
            p.add(year);
            p.add(rating);
            p.add(genres);
            p.add(actors);
            if (s.Description != null) {
                p.add(directors);
            }
            p.add(description);
            p.add(seasons);

            for (Map.Entry<String, List<Episode> > entry : s.episodes.entrySet()) {
                if (entry.getKey() != null) {
                    JLabel episodes = new JLabel(entry.getKey());
                    p.add(episodes);
                    for (Episode i : entry.getValue()) {
                        JLabel title2 = new JLabel("Title: " + i.getTitle());
                        JLabel duration = new JLabel("Duration: " + i.getDuration().toString());
                        p.add(title2);
                        p.add(duration);
                    }
                }
            }

            panel.add(p);
        }
        panel.add(new JLabel("\n"));
        panel.add(new JLabel("Search series after rating"));
        JTextField textField2 = new JTextField(10);
        panel.add(textField2);
        panel.add(searchAfterRate);
        panel.add(new JLabel("Search series after genre"));
        JTextField textField3 = new JTextField(10);
        panel.add(textField3);
        panel.add(searchAfterGenre);
        panel.add(addRate);
        panel.add(back);
        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);

        addRate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddRateSeriesInterface addRateInterface = new AddRateSeriesInterface(imdb, username, type);
                addRateInterface.setVisible(true);
                dispose();
            }
        });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                boolean ok = false;
                for (Series ser : imdb.series) {
                    if (ser.title.equals(s)) {
                        SearchSerial searchserial = new SearchSerial(ser);
                        searchserial.setVisible(true);
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    JOptionPane.showMessageDialog(null, "Series not found!");
                }
            }
        });

        searchAfterRate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField2.getText();
                if (Double.parseDouble(s) < 1 || Double.parseDouble(s) > 10) {
                    JOptionPane.showMessageDialog(null, "Invalid rating!");
                    return;
                }
                boolean ok = false;
                for (Series ser : imdb.series) {
                    if (ser.TotalRating >= Double.parseDouble(s)) {
                        SearchMovieSerialActorRating searchseriesRating = new SearchMovieSerialActorRating(imdb, Double.parseDouble(s), "series");
                        searchseriesRating.setVisible(true);
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    JOptionPane.showMessageDialog(null, "No series were found!");
                }
            }
        });

        searchAfterGenre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField3.getText();
                SearchMovieSerialGenre searchmovieGenre = new SearchMovieSerialGenre(imdb, "series", s);
                searchmovieGenre.setVisible(true);
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductionsInterface productionsInterface = new ProductionsInterface(imdb, username, type);
                productionsInterface.setVisible(true);
                dispose();
            }
        });
    }
}

class AddRateMovieInterface extends JFrame{
    public IMDB imdb;
    String username;
    String type;
    public JButton add;
    public JButton back;
    public AddRateMovieInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JTextField textField = new JTextField(10);
        JTextField textField2 = new JTextField(10);
        JTextField textField3 = new JTextField(10);

        add = new JButton();
        ImageIcon icon = new ImageIcon("images/p10.gif");
        add.setIcon(icon);

        back = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p9.gif");
        back.setIcon(icon3);

        panel.add(Box.createRigidArea(new Dimension(250, 0)));
        panel.add(new JLabel("Add Rate"));
        panel.add(new JLabel("\n"));

        panel.add(new JLabel("Choose a movie number"));
        panel.add(textField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(new JLabel("Choose a rate between 1 and 10"));
        panel.add(textField2);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(new JLabel("Type a comment"));
        panel.add(textField3);
        panel.add(add);
        panel.add(back);
        add(panel);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                String s2 = textField2.getText();
                Integer nr = Integer.parseInt(s);
                Integer rate = Integer.parseInt(s2);
                String comm = textField3.getText();
                if (rate < 1 || rate > 10) {
                    JOptionPane.showMessageDialog(null, "Invalid rate! Rate must be between 1 and 10!");
                    return;
                }
                Integer movieNumber = Integer.parseInt(s);
                if (movieNumber < 1 || movieNumber > imdb.movie.size()) {
                    JOptionPane.showMessageDialog(null, "Invalid movie number!");
                    return;
                }

                Rating r = new Rating(username, rate, comm);

                Integer nr3 = 1;
                for (Movie m : imdb.movie) {
                    if (nr3.equals(nr)) {
                        m.Ratings.add(r);
                        m.TotalRating = m.CalculateRating();
                        ExprerienceStrategy xp = new addReviewExperience();
                        for (Regular reg : imdb.regular) {
                            if (reg.username.equals(username)) {
                                reg.setExprerienceStrategy(xp);
                                reg.updateExperience();
                                break;
                            }
                        }
                        for (Admin am : imdb.admin) {
                            if (am.username.equals(username)) {
                                am.setExprerienceStrategy(xp);
                                am.updateExperience();
                                break;
                            }
                        }
                        for (Contributor contr : imdb.contributor) {
                            if (contr.username.equals(username)) {
                                contr.setExprerienceStrategy(xp);
                                contr.updateExperience();
                                break;
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Rating added successfully!");
                        break;
                    }
                    nr3++;
                }
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MoviesInterface moviesInterface = new MoviesInterface(imdb, username, type);
                moviesInterface.setVisible(true);
                dispose();
            }
        });
    }
}

class AddRateSeriesInterface extends JFrame{
    public IMDB imdb;
    String username;
    String type;
    public JButton add;
    public JButton back;
    public AddRateSeriesInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JTextField textField = new JTextField(10);
        JTextField textField2 = new JTextField(10);
        JTextField textField3 = new JTextField(10);

        add = new JButton();
        ImageIcon icon = new ImageIcon("images/p10.gif");
        add.setIcon(icon);

        back = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p9.gif");
        back.setIcon(icon3);

        panel.add(Box.createRigidArea(new Dimension(250, 0)));
        panel.add(new JLabel("Add Rate"));
        panel.add(new JLabel("\n"));

        panel.add(new JLabel("Choose a series number"));
        panel.add(textField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(new JLabel("Choose a rate between 1 and 10"));
        panel.add(textField2);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(new JLabel("Type a comment"));
        panel.add(textField3);
        panel.add(add);
        panel.add(back);
        add(panel);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                String s2 = textField2.getText();
                Integer nr = Integer.parseInt(s);
                Integer rate = Integer.parseInt(s2);
                String comm = textField3.getText();
                if (rate < 1 || rate > 10) {
                    JOptionPane.showMessageDialog(null, "Invalid rate! Rate must be between 1 and 10!");
                    return;
                }
                Integer movieNumber = Integer.parseInt(s);
                if (movieNumber < 1 || movieNumber > imdb.movie.size()) {
                    JOptionPane.showMessageDialog(null, "Invalid movie number!");
                    return;
                }

                Rating r = new Rating(username, rate, comm);

                Integer nr3 = 1;
                for (Series ser : imdb.series) {
                    if (nr3.equals(nr)) {
                        ser.Ratings.add(r);
                        ser.TotalRating = ser.CalculateRating();
                        ExprerienceStrategy xp = new addReviewExperience();
                        for (Regular reg : imdb.regular) {
                            if (reg.username.equals(username)) {
                                reg.setExprerienceStrategy(xp);
                                reg.updateExperience();
                                break;
                            }
                        }
                        for (Admin am : imdb.admin) {
                            if (am.username.equals(username)) {
                                am.setExprerienceStrategy(xp);
                                am.updateExperience();
                                break;
                            }
                        }
                        for (Contributor contr : imdb.contributor) {
                            if (contr.username.equals(username)) {
                                contr.setExprerienceStrategy(xp);
                                contr.updateExperience();
                                break;
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Rating added successfully!");
                        break;
                    }
                    nr3++;
                }
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SeriesInterface seriesInterface = new SeriesInterface(imdb, username, type);
                seriesInterface.setVisible(true);
                dispose();
            }
        });
    }
}

class AddRateActorsInterface extends JFrame{
    public IMDB imdb;
    String username;
    String type;
    public JButton add;
    public JButton back;
    public AddRateActorsInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JTextField textField = new JTextField(10);
        JTextField textField2 = new JTextField(10);
        JTextField textField3 = new JTextField(10);

        add = new JButton();
        ImageIcon icon = new ImageIcon("images/p10.gif");
        add.setIcon(icon);

        back = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p9.gif");
        back.setIcon(icon3);

        panel.add(Box.createRigidArea(new Dimension(250, 0)));
        panel.add(new JLabel("Add Rate"));
        panel.add(new JLabel("\n"));

        panel.add(new JLabel("Choose an actor number"));
        panel.add(textField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(new JLabel("Choose a rate between 1 and 10"));
        panel.add(textField2);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(new JLabel("Type a comment"));
        panel.add(textField3);
        panel.add(add);
        panel.add(back);
        add(panel);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                String s2 = textField2.getText();
                Integer nr = Integer.parseInt(s);
                Integer rate = Integer.parseInt(s2);
                String comm = textField3.getText();
                if (rate < 1 || rate > 10) {
                    JOptionPane.showMessageDialog(null, "Invalid rate! Rate must be between 1 and 10!");
                    return;
                }
                Integer actorNumber = Integer.parseInt(s);
                if (actorNumber < 1 || actorNumber > imdb.movie.size()) {
                    JOptionPane.showMessageDialog(null, "Invalid actor number!");
                    return;
                }

                Rating r = new Rating(username, rate, comm);

                Integer nr3 = 1;
                for (Actor act : imdb.actor) {
                    if (nr3.equals(nr)) {
                        act.Ratings.add(r);
                        act.TotalRating = act.CalculateRating();
                        ExprerienceStrategy xp = new addReviewExperience();
                        for (Regular reg : imdb.regular) {
                            if (reg.username.equals(username)) {
                                reg.setExprerienceStrategy(xp);
                                reg.updateExperience();
                                break;
                            }
                        }
                        for (Admin am : imdb.admin) {
                            if (am.username.equals(username)) {
                                am.setExprerienceStrategy(xp);
                                am.updateExperience();
                                break;
                            }
                        }
                        for (Contributor contr : imdb.contributor) {
                            if (contr.username.equals(username)) {
                                contr.setExprerienceStrategy(xp);
                                contr.updateExperience();
                                break;
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Rating added successfully!");
                        break;
                    }
                    nr3++;
                }
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActorsInterface actInterface = new ActorsInterface(imdb, username, type);
                actInterface.setVisible(true);
                dispose();
            }
        });
    }
}

class SearchMovie extends JFrame{
    Movie m;
    public SearchMovie(Movie m) {
        this.m = m;

        setTitle("IMDB");
        setSize(1000, 600);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();

        JLabel title = new JLabel("Title: " + m.getTitle());
        JLabel year = new JLabel("Release Year: " + m.getYear_of_Release().toString());
        JLabel rating = new JLabel("Rating: " + m.getTotalRating().toString());
        JLabel genres = new JLabel("Genres: " + m.getGenres().toString());
        JLabel duration = new JLabel("Duration: " + m.getDuration().toString());
        JLabel actors = new JLabel("Actors: " + m.getActors().toString());
        JLabel directors = new JLabel("Directors: " + m.getDirectors().toString());
        JLabel description = new JLabel("Description: " + m.getDescription());
        JPanel p = new JPanel();
        p.add(title);
        p.add(year);
        p.add(rating);
        p.add(genres);
        p.add(duration);
        p.add(actors);
        p.add(directors);
        p.add(description);
        panel.add(p);
        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

class SearchSerial extends JFrame{
    Series s;
    public SearchSerial(Series s) {
        this.s = s;

        setTitle("IMDB");
        setSize(1000, 600);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();

        JLabel title = new JLabel("Title: " + s.getTitle());
        JLabel year = new JLabel("Release Year: " + s.getYear().toString());
        JLabel rating = new JLabel("Rating: " + s.getTotalRating().toString());
        JLabel genres = new JLabel("Genres: " + s.getGenres().toString());
        JLabel actors = new JLabel("Actors: " + s.getActors().toString());
        JLabel directors = new JLabel("Directors: " + s.getDirectors().toString());
        JLabel description = new JLabel("Description: " + s.getDescription());
        JLabel seasons = new JLabel("Number of seasons: " + s.getNumberOfSeasons().toString());

        JPanel p = new JPanel();
        p.add(title);
        p.add(year);
        p.add(rating);
        p.add(genres);
        p.add(actors);
        p.add(directors);
        p.add(description);
        p.add(seasons);

        for (Map.Entry<String, List<Episode> > entry : s.episodes.entrySet()) {
            if (entry.getKey() != null) {
                JLabel episodes = new JLabel(entry.getKey());
                p.add(episodes);
                for (Episode i : entry.getValue()) {
                    JLabel title2 = new JLabel("Title: " + i.getTitle());
                    JLabel duration = new JLabel("Duration: " + i.getDuration().toString());
                    p.add(title2);
                    p.add(duration);
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);
        add(p);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

class SearchActor extends JFrame{
    Actor a;
    public SearchActor(Actor a) {
        this.a = a;

        setTitle("IMDB");
        setSize(1000, 600);
        setResizable(false);

        JPanel panel = new JPanel();

        JLabel name = new JLabel("Name: " + a.getName());
        JLabel bio = new JLabel("Biography: " + a.getBiography());
        double ratng = a.getTotalRating();
        String rating = String.valueOf(ratng);
        JLabel rating2 = new JLabel("Rating: " + rating);

        JPanel p = new JPanel();
        p.add(name);
        p.add(bio);
        p.add(rating2);
        p.add(new JLabel("Performances: "));
        for (String key : a.roles.keySet()) {
            p.add(new JLabel(key + " - " + a.roles.get(key)));
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane);
        add(p);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

class SearchMovieSerialActorRating extends JFrame{
    public IMDB imdb;
    double rt;
    String type;
    public SearchMovieSerialActorRating(IMDB imdb, double rt, String type) {
        this.imdb = imdb;
        this.rt = rt;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (type.equals("movie")) {
            for (Movie m : imdb.movie) {
                if (m.TotalRating >= rt) {
                    JLabel title = new JLabel("Title: " + m.getTitle());
                    JLabel year = new JLabel("Release Year: " + m.getYear_of_Release().toString());
                    JLabel rating = new JLabel("Rating: " + m.getTotalRating().toString());
                    JLabel genres = new JLabel("Genres: " + m.getGenres().toString());
                    JLabel duration = new JLabel("Duration: " + m.getDuration().toString());
                    JLabel actors = new JLabel("Actors: " + m.getActors().toString());
                    JLabel directors = new JLabel("Directors: " + m.getDirectors().toString());
                    JLabel description = new JLabel("Description: " + m.getDescription());
                    JPanel p = new JPanel();
                    p.add(title);
                    p.add(year);
                    p.add(rating);
                    p.add(genres);
                    p.add(duration);
                    p.add(actors);
                    p.add(directors);
                    if (m.Description != null) {
                        p.add(description);
                    }
                    p.setLayout(new FlowLayout(FlowLayout.LEFT));
                    panel.add(p);
                    panel.add(new JLabel("\n"));
                    add(panel);
                }
            }
        }
        else if (type.equals("series")) {
            for (Series s : imdb.series) {
                if (s.TotalRating >= rt) {
                    JLabel title = new JLabel("Title: " + s.getTitle());
                    JLabel year = new JLabel("Release Year: " + s.getYear().toString());
                    JLabel rating = new JLabel("Rating: " + s.getTotalRating().toString());
                    JLabel genres = new JLabel("Genres: " + s.getGenres().toString());
                    JLabel actors = new JLabel("Actors: " + s.getActors().toString());
                    JLabel directors = new JLabel("Directors: " + s.getDirectors().toString());
                    JLabel description = new JLabel("Description: " + s.getDescription());
                    JLabel seasons = new JLabel("Number of seasons: " + s.getNumberOfSeasons().toString());

                    JPanel p = new JPanel();
                    p.add(title);
                    p.add(year);
                    p.add(rating);
                    p.add(genres);
                    p.add(actors);
                    if (s.Description != null) {
                        p.add(directors);
                    }
                    p.add(description);
                    p.add(seasons);

                    for (Map.Entry<String, List<Episode>> entry : s.episodes.entrySet()) {
                        if (entry.getKey() != null) {
                            JLabel episodes = new JLabel(entry.getKey());
                            p.add(episodes);
                            for (Episode i : entry.getValue()) {
                                JLabel title2 = new JLabel("Title: " + i.getTitle());
                                JLabel duration = new JLabel("Duration: " + i.getDuration().toString());
                                p.add(title2);
                                p.add(duration);
                            }
                        }
                    }
                    p.setLayout(new FlowLayout(FlowLayout.LEFT));
                    panel.add(p);
                    panel.add(new JLabel("\n"));
                    add(panel);
                }
            }
        } else if (type.equals("actors")) {
            for (Actor a : imdb.actor) {
                if (a.TotalRating >= rt) {
                    JLabel name = new JLabel("Name: " + a.getName());
                    JLabel bio = new JLabel("Biography: " + a.getBiography());
                    double ratng = a.getTotalRating();
                    String rating = String.valueOf(ratng);
                    JLabel rating2 = new JLabel("Rating: " + rating);

                    JPanel p = new JPanel();
                    p.add(name);
                    p.add(bio);
                    p.add(rating2);
                    p.add(new JLabel("Performances: "));
                    for (String key : a.roles.keySet()) {
                        p.add(new JLabel(key + " - " + a.roles.get(key)));
                    }
                    p.setLayout(new FlowLayout(FlowLayout.LEFT));
                    panel.add(p);
                    panel.add(new JLabel("\n"));
                    add(panel);
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

class AddFavoritesInterface extends JFrame{
    IMDB imdb;
    String username;
    JButton add;
    public AddFavoritesInterface(IMDB imdb, String username) {
        this.imdb = imdb;
        this.username = username;

        setTitle("IMDB");
        setSize(1000, 600);
        setResizable(false);

        add = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p11.gif");
        add.setIcon(icon3);

        JPanel panel = new JPanel();

        panel.add(new JLabel("Write the name of the movie/series/actor you want to add to favorites"));
        JTextField textField = new JTextField(10);
        panel.add(textField);
        panel.add(add);

        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                for (Movie m : imdb.movie) {
                    if (m.title.equals(s)) {
                        for (Regular reg : imdb.regular) {
                            if (reg.username.equals(username)) {
                                reg.addFavorite(s);
                                JOptionPane.showMessageDialog(null, "Movie added to favorites!");
                                return;
                            }
                        }
                        for (Contributor contr : imdb.contributor) {
                            if (contr.username.equals(username)) {
                                contr.addFavorite(s);
                                JOptionPane.showMessageDialog(null, "Movie added to favorites!");
                                return;
                            }
                        }
                        for (Admin am : imdb.admin) {
                            if (am.username.equals(username)) {
                                am.addFavorite(s);
                                JOptionPane.showMessageDialog(null, "Movie added to favorites!");
                                return;
                            }
                        }
                    }
                }
                for (Series ser : imdb.series) {
                    if (ser.title.equals(s)) {
                        for (Regular reg : imdb.regular) {
                            if (reg.username.equals(username)) {
                                reg.addFavorite(s);
                                JOptionPane.showMessageDialog(null, "Series added to favorites!");
                                return;
                            }
                        }
                        for (Contributor contr : imdb.contributor) {
                            if (contr.username.equals(username)) {
                                contr.addFavorite(s);
                                JOptionPane.showMessageDialog(null, "Series added to favorites!");
                                return;
                            }
                        }
                        for (Admin am : imdb.admin) {
                            if (am.username.equals(username)) {
                                am.addFavorite(s);
                                JOptionPane.showMessageDialog(null, "Series added to favorites!");
                                return;
                            }
                        }
                    }
                }
                for (Actor act : imdb.actor) {
                    if (act.name.equals(s)) {
                        for (Regular reg : imdb.regular) {
                            if (reg.username.equals(username)) {
                                reg.addFavorite(s);
                                JOptionPane.showMessageDialog(null, "Actor added to favorites!");
                                return;
                            }
                        }
                        for (Contributor contr : imdb.contributor) {
                            if (contr.username.equals(username)) {
                                contr.addFavorite(s);
                                JOptionPane.showMessageDialog(null, "Actor added to favorites!");
                                return;
                            }
                        }
                        for (Admin am : imdb.admin) {
                            if (am.username.equals(username)) {
                                am.addFavorite(s);
                                JOptionPane.showMessageDialog(null, "Actor added to favorites!");
                                return;
                            }
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Invalid production!");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

class RemoveFavoritesInterface extends JFrame{
    IMDB imdb;
    String username;
    JButton remove;
    public RemoveFavoritesInterface(IMDB imdb, String username) {
        this.imdb = imdb;
        this.username = username;

        setTitle("IMDB");
        setSize(1000, 600);
        setResizable(false);

        remove = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p13.gif");
        remove.setIcon(icon3);

        JPanel panel = new JPanel();

        panel.add(new JLabel("Write the name of the movie/series/actor you want to remove from favorites"));
        JTextField textField = new JTextField(10);
        panel.add(textField);
        panel.add(remove);

        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane);

        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                for (Regular r : imdb.regular) {
                    if (r.username.equals(username)) {
                        for (Object o : r.favorites) {
                            if (o.equals(s)) {
                                r.removeFavorite(s);
                                JOptionPane.showMessageDialog(null, "Production removed from favorites!");
                                return;
                            }
                        }
                    }
                }
                for (Contributor c : imdb.contributor) {
                    if (c.username.equals(username)) {
                        for (Object o : c.favorites) {
                            if (o.equals(s)) {
                                c.removeFavorite(s);
                                JOptionPane.showMessageDialog(null, "Production removed from favorites!");
                                return;
                            }
                        }
                    }
                }
                for (Admin adm : imdb.admin) {
                    if (adm.username.equals(username)) {
                        for (Object o : adm.favorites) {
                            if (o.equals(s)) {
                                adm.removeFavorite(s);
                                JOptionPane.showMessageDialog(null, "Production removed from favorites!");
                                return;
                            }
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Invalid production!");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

class ViewRequestsInterface extends JFrame {
    IMDB imdb;
    String username;
    String type;
    public JButton back;
    public JButton add;

    public ViewRequestsInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add = new JButton();
        ImageIcon icon = new ImageIcon("images/p11.gif");
        add.setIcon(icon);

        back = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p9.gif");
        back.setIcon(icon3);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        int nr2 = 0;
        panel.add(new JLabel("Requests: "));
        for (Contributor c : imdb.contributor) {
            if (c.username.equals(username)) {
                Integer nr = 1;
                for (Object r : c.r) {
                    JPanel p = new JPanel();
                    p.add(new JLabel(nr.toString() + ") "));
                    Request req = (Request) r;
                    p.add(new JLabel(req.requestType.toString()));
                    p.add(new JLabel(req.name));
                    p.add(new JLabel(req.description));
                    p.add(new JLabel(req.username_requester));
                    nr = nr + 1;
                    panel.add(p);
                }
                nr2 = nr - 1;
                break;
            }
        }

        panel.add(new JLabel("\n"));
        panel.add(new JLabel("Select a number request to accept:"));
        JTextField textField = new JTextField(10);
        panel.add(textField);
        panel.add(new JLabel("Resolve request?"));
        JTextField textField2 = new JTextField(10);
        panel.add(textField2);
        panel.add(add);

        panel.add(back);
        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);

        int finalNr = nr2;
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                String s2 = textField2.getText();
                if (Integer.parseInt(s) > finalNr || Integer.parseInt(s) < 1) {
                    JOptionPane.showMessageDialog(null, "Invalid number!");
                    return;
                }
                for (Contributor c : imdb.contributor) {
                    if (c.username.equals(username)) {
                        Integer nr3 = 1;
                        for (Object o : c.r) {
                            if (Integer.parseInt(s) == nr3) {
                                Request req = (Request) o;
                                c.r.remove(o);
                                String usr = req.username_requester;
                                for (Regular r : imdb.regular) {
                                    if (r.username.equals(usr)) {
                                        if (s2.equals("yes")) {
                                            r.notifications.add("Request denied!");
                                            JOptionPane.showMessageDialog(null, "Request denied!");
                                            return;
                                        }
                                        ExprerienceStrategy xp = new addRequestExperience();
                                        r.setExprerienceStrategy(xp);
                                        r.updateExperience();
                                        r.notifications.add("Request resolved!");
                                        JOptionPane.showMessageDialog(null, "Request denied!");
                                        break;
                                    }
                                }
                                for (Contributor cont : imdb.contributor) {
                                    if (cont.username.equals(usr)) {
                                        if (s2.equals("yes")) {
                                            cont.notifications.add("Request denied!");
                                            JOptionPane.showMessageDialog(null, "Request denied!");
                                            return;
                                        }
                                        ExprerienceStrategy xp = new addRequestExperience();
                                        cont.setExprerienceStrategy(xp);
                                        cont.updateExperience();
                                        cont.notifications.add("Request resolved!");
                                        break;
                                    }
                                }
                                break;
                            }
                            nr3 = nr3 + 1;
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Request accepted!");
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type.equals("regular")) {
                    RegUserInterface regUserInterface = new RegUserInterface(imdb, username);
                    regUserInterface.setVisible(true);
                    dispose();
                } else if (type.equals("contributor")) {
                    ContrUserInterface contributorInterface = new ContrUserInterface(imdb, username);
                    contributorInterface.setVisible(true);
                    dispose();
                } else if (type.equals("admin")) {
                    AdminUserInterface adminInterface = new AdminUserInterface(imdb, username);
                    adminInterface.setVisible(true);
                    dispose();
                }
            }
        });

    }
}

class ViewNotificationsInterface extends JFrame{
    public JButton back;

    IMDB imdb;
    String username;
    String type;

    public ViewNotificationsInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        back = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p9.gif");
        back.setIcon(icon3);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (type.equals("regular")) {
            for (Regular r : imdb.regular) {
                if (r.username.equals(username)) {
                    if (r.notifications.isEmpty()) {
                        JPanel p = new JPanel();
                        p.add(new JLabel("No notifications!"));
                        panel.add(p);
                        break;
                    }
                    Integer nr = 1;
                    for (Object not : r.notifications) {
                        JPanel p = new JPanel();
                        p.add(new JLabel(nr.toString() + ") "));
                        String n = (String) not;
                        p.add(new JLabel(n));
                        nr = nr + 1;
                        panel.add(p);
                    }
                }
            }
        }

        if (type.equals("contributor")) {
            for (Contributor c : imdb.contributor) {
                if (c.username.equals(username)) {
                    if (c.notifications.isEmpty()) {
                        JPanel p = new JPanel();
                        p.add(new JLabel("No notifications!"));
                        panel.add(p);
                        break;
                    }
                    Integer nr = 1;
                    for (Object not : c.notifications) {
                        JPanel p = new JPanel();
                        p.add(new JLabel(nr.toString() + ") "));
                        String n = (String) not;
                        p.add(new JLabel(n));
                        nr = nr + 1;
                        panel.add(p);
                    }
                }
            }
        }

        if (type.equals("admin")) {
            for (Admin a : imdb.admin) {
                if (a.username.equals(username)) {
                    if (a.notifications.isEmpty()) {
                        JPanel p = new JPanel();
                        p.add(new JLabel("No notifications!"));
                        panel.add(p);
                        break;
                    }
                    Integer nr = 1;
                    for (Object not : a.notifications) {
                        JPanel p = new JPanel();
                        p.add(new JLabel(nr.toString() + ") "));
                        String n = (String) not;
                        p.add(new JLabel(n));
                        nr = nr + 1;
                        panel.add(p);
                    }
                }
            }
        }


        panel.add(back);
        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type.equals("regular")) {
                    RegUserInterface regUserInterface = new RegUserInterface(imdb, username);
                    regUserInterface.setVisible(true);
                    dispose();
                } else if (type.equals("contributor")) {
                    ContrUserInterface contributorInterface = new ContrUserInterface(imdb, username);
                    contributorInterface.setVisible(true);
                    dispose();
                } else if (type.equals("admin")) {
                    AdminUserInterface adminInterface = new AdminUserInterface(imdb, username);
                    adminInterface.setVisible(true);
                    dispose();
                }
            }
        });
    }
}

class AddProductionInterface extends JFrame{
    public JButton back;
    public JButton add;
    public JButton remove;
    public JButton add2;
    public JButton add3;

    IMDB imdb;
    String username;
    String type;

    public AddProductionInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add = new JButton();
        ImageIcon icon = new ImageIcon("images/p11.gif");
        add.setIcon(icon);

        add2 = new JButton();
        ImageIcon icon4 = new ImageIcon("images/p11.gif");
        add2.setIcon(icon4);

        add3 = new JButton();
        ImageIcon icon5 = new ImageIcon("images/p11.gif");
        add3.setIcon(icon5);

        remove = new JButton();
        ImageIcon icon2 = new ImageIcon("images/p13.gif");
        remove.setIcon(icon2);

        back = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p9.gif");
        back.setIcon(icon3);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Add a movie: "));
        panel.add(add);
        panel.add(new JLabel("Add a series: "));
        panel.add(add2);
        panel.add(new JLabel("Add an actor: "));
        panel.add(add3);
        panel.add(new JLabel("Remove a production: "));
        panel.add(remove);
        panel.add(back);
        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMovieInterface addmov = new AddMovieInterface(imdb, username, type);
                addmov.setVisible(true);
            }
        });

        add2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSeriesInterface addser = new AddSeriesInterface(imdb, username, type);
                addser.setVisible(true);
            }
        });

        add3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddActorInterface addactor = new AddActorInterface(imdb, username, type);
                addactor.setVisible(true);
            }
        });

        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RemoveProductionInterface removeProd = new RemoveProductionInterface(imdb, username, type);
                removeProd.setVisible(true);
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type.equals("regular")) {
                    RegUserInterface regUserInterface = new RegUserInterface(imdb, username);
                    regUserInterface.setVisible(true);
                    dispose();
                } else if (type.equals("contributor")) {
                    ContrUserInterface contributorInterface = new ContrUserInterface(imdb, username);
                    contributorInterface.setVisible(true);
                    dispose();
                } else if (type.equals("admin")) {
                    AdminUserInterface adminInterface = new AdminUserInterface(imdb, username);
                    adminInterface.setVisible(true);
                    dispose();
                }
            }
        });

    }
}

class AddMovieInterface extends JFrame{
    IMDB imdb;
    String username;
    String type;
    public JButton add1;
    public JButton add2;
    public JButton add3;
    public JButton add;

    public AddMovieInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add1 = new JButton();
        ImageIcon icon1 = new ImageIcon("images/p11.gif");
        add1.setIcon(icon1);

        add2 = new JButton();
        ImageIcon icon2 = new ImageIcon("images/p11.gif");
        add2.setIcon(icon2);

        add3 = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p11.gif");
        add3.setIcon(icon3);

        add = new JButton();
        ImageIcon icon4 = new ImageIcon("images/p11.gif");
        add.setIcon(icon4);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField textField = new JTextField(10);
        JTextField textField2 = new JTextField(10);
        JTextField textField3 = new JTextField(10);
        JTextField textField4 = new JTextField(10);
        JTextField textField5 = new JTextField(10);
        JTextField textField6 = new JTextField(10);
        JTextField textField7 = new JTextField(10);

        panel.add(new JLabel("Type title"));
        panel.add(textField);
        panel.add(new JLabel("Type year of release"));
        panel.add(textField2);
        panel.add(new JLabel("Type genres"));
        panel.add(textField3);
        panel.add(add1);
        panel.add(new JLabel("Type duration"));
        panel.add(textField4);
        panel.add(new JLabel("Type actors"));
        panel.add(textField5);
        panel.add(add2);
        panel.add(new JLabel("Type directors"));
        panel.add(textField6);
        panel.add(add3);
        panel.add(new JLabel("Type description"));
        panel.add(textField7);
        panel.add(add);

        add(panel);

        List<Genre> Genres = new ArrayList<Genre>();
        List<String> Actors = new ArrayList<String>();
        List<String> Directors = new ArrayList<String>();

        add1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField3.getText();
                if (!s.isEmpty()) {
                    textField3.setText("");
                    if (s.equals("action")) {
                        Genres.add((Genre.Action));
                        JOptionPane.showMessageDialog(null, "Genre added!");
                    } else if (s.equals("comedy")) {
                        Genres.add((Genre.Comedy));
                        JOptionPane.showMessageDialog(null, "Genre added!");
                    } else if (s.equals("drama")) {
                        Genres.add((Genre.Drama));
                        JOptionPane.showMessageDialog(null, "Genre added!");
                    } else if (s.equals("horror")) {
                        Genres.add((Genre.Horror));
                        JOptionPane.showMessageDialog(null, "Genre added!");
                    } else if (s.equals("thriller")) {
                        Genres.add((Genre.Thriller));
                        JOptionPane.showMessageDialog(null, "Genre added!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid genre!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Type a genre!");
                }
            }
        });

        add2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField5.getText();
                if (!s.isEmpty()) {
                    Actors.add(s);
                    textField5.setText("");
                    JOptionPane.showMessageDialog(null, "Actor added!");
                } else {
                    JOptionPane.showMessageDialog(null, "Type an actor!");
                }
            }
        });

        add3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField6.getText();
                if (!s.isEmpty()) {
                    Directors.add(s);
                    textField6.setText("");
                    JOptionPane.showMessageDialog(null, "Director added!");
                } else {
                    JOptionPane.showMessageDialog(null, "Type a director!");
                }
            }
        });

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = textField.getText();
                String year = textField2.getText();
                Integer year2 = Integer.parseInt(year);
                String duration = textField4.getText();
                Integer duration2 = Integer.parseInt(duration);
                String description = textField7.getText();
                Movie m = new Movie(title, Directors, Actors, Genres, null, description, duration2, duration2);
                imdb.movie.add(m);
                for (Contributor c : imdb.contributor) {
                    if (c.username.equals(username)) {
                        ExprerienceStrategy xp = new addNewProductActorExperience();
                        c.setExprerienceStrategy(xp);
                        c.updateExperience();
                        c.notifications.add("Production added!");
                        break;
                    }
                }
                for (Admin a : imdb.admin) {
                    if (a.username.equals(username)) {
                        ExprerienceStrategy xp = new addNewProductActorExperience();
                        a.setExprerienceStrategy(xp);
                        a.updateExperience();
                        a.notifications.add("Production added!");
                        break;
                    }
                }
                JOptionPane.showMessageDialog(null, "Movie added!");
                return;
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

class AddSeriesInterface extends JFrame{
    IMDB imdb;
    String username;
    String type;
    public JButton add1;
    public JButton add2;
    public JButton add3;
    public JButton add;
    public JButton add4;
    public JButton add5;

    public AddSeriesInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add1 = new JButton();
        ImageIcon icon1 = new ImageIcon("images/p11.gif");
        add1.setIcon(icon1);

        add2 = new JButton();
        ImageIcon icon2 = new ImageIcon("images/p11.gif");
        add2.setIcon(icon2);

        add3 = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p11.gif");
        add3.setIcon(icon3);

        add = new JButton();
        ImageIcon icon4 = new ImageIcon("images/p11.gif");
        add.setIcon(icon4);

        add4 = new JButton();
        ImageIcon icon5 = new ImageIcon("images/p11.gif");
        add4.setIcon(icon5);

        add5 = new JButton();
        ImageIcon icon6 = new ImageIcon("images/p11.gif");
        add5.setIcon(icon6);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField textField = new JTextField(10);
        JTextField textField2 = new JTextField(10);
        JTextField textField3 = new JTextField(10);
        JTextField textField4 = new JTextField(10);
        JTextField textField5 = new JTextField(10);
        JTextField textField6 = new JTextField(10);
        JTextField textField7 = new JTextField(10);
        JTextField textField8 = new JTextField(10);
        JTextField textField9 = new JTextField(10);
        JTextField textField10 = new JTextField(10);

        panel.add(new JLabel("Type title"));
        panel.add(textField);
        panel.add(new JLabel("Type year of release"));
        panel.add(textField2);
        panel.add(new JLabel("Type genres"));
        panel.add(textField3);
        panel.add(add1);
        panel.add(new JLabel("Type actors"));
        panel.add(textField4);
        panel.add(add2);
        panel.add(new JLabel("Type directors"));
        panel.add(textField5);
        panel.add(add3);
        panel.add(new JLabel("Type description"));
        panel.add(textField6);
        panel.add(new JLabel("Type number of seasons"));
        panel.add(textField7);
        panel.add(new JLabel("Season number"));
        panel.add(textField8);
        panel.add(new JLabel("Type episode name"));
        panel.add(textField9);
        panel.add(new JLabel("Type episode duration"));
        panel.add(textField10);
        panel.add(add4);
        panel.add(new JLabel("Add season"));
        panel.add(add5);
        panel.add(add);

//        JScrollPane scrollPane = new JScrollPane(panel);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        add(scrollPane);

        add(panel);

        List<Genre> Genres = new ArrayList<Genre>();
        List<String> Actors = new ArrayList<String>();
        List<String> Directors = new ArrayList<String>();
        Map<String, List<Episode>> episodes = new HashMap<String, List<Episode>>();

        add1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField3.getText();
                if (!s.isEmpty()) {
                    textField3.setText("");
                    if (s.equals("action")) {
                        Genres.add((Genre.Action));
                        JOptionPane.showMessageDialog(null, "Genre added!");
                    } else if (s.equals("comedy")) {
                        Genres.add((Genre.Comedy));
                        JOptionPane.showMessageDialog(null, "Genre added!");
                    } else if (s.equals("drama")) {
                        Genres.add((Genre.Drama));
                        JOptionPane.showMessageDialog(null, "Genre added!");
                    } else if (s.equals("horror")) {
                        Genres.add((Genre.Horror));
                        JOptionPane.showMessageDialog(null, "Genre added!");
                    } else if (s.equals("thriller")) {
                        Genres.add((Genre.Thriller));
                        JOptionPane.showMessageDialog(null, "Genre added!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid genre!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Type a genre!");
                }
            }
        });

        add2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField4.getText();
                if (!s.isEmpty()) {
                    Actors.add(s);
                    textField4.setText("");
                    JOptionPane.showMessageDialog(null, "Actor added!");
                } else {
                    JOptionPane.showMessageDialog(null, "Type an actor!");
                }
            }
        });

        add3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField5.getText();
                if (!s.isEmpty()) {
                    Directors.add(s);
                    textField5.setText("");
                    JOptionPane.showMessageDialog(null, "Director added!");
                } else {
                    JOptionPane.showMessageDialog(null, "Type a director!");
                }
            }
        });

        List<Episode> eps = new ArrayList<Episode>();
        add4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s2 = textField9.getText();
                String s3 = textField10.getText();
                if (!s2.isEmpty() && !s3.isEmpty()) {
                    textField9.setText("");
                    textField10.setText("");
                    Episode ep = new Episode(s2, Integer.parseInt(s3));
                    eps.add(ep);
                    JOptionPane.showMessageDialog(null, "Episode added!");
                } else {
                    JOptionPane.showMessageDialog(null, "Type an episode!");
                }
            }
        });

        add5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField8.getText();
                if (!s.isEmpty()) {
                    textField8.setText("");
                    episodes.put(s, eps);
                    eps.clear();
                    JOptionPane.showMessageDialog(null, "Season added!");
                } else {
                    JOptionPane.showMessageDialog(null, "Type a season!");
                }
            }
        });

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = textField.getText();
                String year = textField2.getText();
                Integer year2 = Integer.parseInt(year);
                String description = textField6.getText();
                String numberofSes = textField7.getText();
                Integer numberofSes2 = Integer.parseInt(numberofSes);
                Series s = new Series(title, Directors, Actors, Genres, null, description, year2, numberofSes2, episodes);
                imdb.series.add(s);
                for (Contributor c : imdb.contributor) {
                    if (c.username.equals(username)) {
                        ExprerienceStrategy xp = new addNewProductActorExperience();
                        c.setExprerienceStrategy(xp);
                        c.updateExperience();
                        c.notifications.add("Production added!");
                        break;
                    }
                }
                for (Admin a : imdb.admin) {
                    if (a.username.equals(username)) {
                        ExprerienceStrategy xp = new addNewProductActorExperience();
                        a.setExprerienceStrategy(xp);
                        a.updateExperience();
                        a.notifications.add("Production added!");
                        break;
                    }
                }
                JOptionPane.showMessageDialog(null, "Series added!");
                return;
            }
        });

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

class AddActorInterface extends JFrame{

    IMDB imdb;
    String username;
    String type;
    public JButton add1;
    public JButton add;

    public AddActorInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setResizable(false);

        add1 = new JButton();
        ImageIcon icon1 = new ImageIcon("images/p11.gif");
        add1.setIcon(icon1);

        add = new JButton();
        ImageIcon icon4 = new ImageIcon("images/p11.gif");
        add.setIcon(icon4);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField textField = new JTextField(10);
        JTextField textField2 = new JTextField(10);
        JTextField textField3 = new JTextField(10);
        JTextField textField4 = new JTextField(10);

        panel.add(new JLabel("Type name"));
        panel.add(textField);
        panel.add(new JLabel("Type biography"));
        panel.add(textField2);
        panel.add(new JLabel("Type Movie or Series"));
        panel.add(textField3);
        panel.add(new JLabel("Type Movie title"));
        panel.add(textField4);
        panel.add(add1);
        panel.add(add);

        add(panel);

        Map<String, String> Roles = new HashMap<String, String>();

        add1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField3.getText();
                String s2 = textField4.getText();
                if (!s2.isEmpty()) {
                    textField4.setText("");
                    Roles.put(s, s2);
                } else {
                    JOptionPane.showMessageDialog(null, "Type a genre!");
                }
            }
        });

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textField.getText();
                String biography = textField2.getText();
                Actor a = new Actor(name, Roles, biography);
                imdb.actor.add(a);
                for (Contributor c : imdb.contributor) {
                    if (c.username.equals(username)) {
                        ExprerienceStrategy xp = new addNewProductActorExperience();
                        c.setExprerienceStrategy(xp);
                        c.updateExperience();
                        c.notifications.add("Production added!");
                        break;
                    }
                }
                for (Admin adm : imdb.admin) {
                    if (adm.username.equals(username)) {
                        ExprerienceStrategy xp = new addNewProductActorExperience();
                        adm.setExprerienceStrategy(xp);
                        adm.updateExperience();
                        adm.notifications.add("Production added!");
                        break;
                    }
                }
                JOptionPane.showMessageDialog(null, "Actor added!");
                return;
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

class RemoveProductionInterface extends JFrame{
    IMDB imdb;
    String username;
    String type;
    public JButton add;

    public RemoveProductionInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setResizable(false);


        add = new JButton();
        ImageIcon icon4 = new ImageIcon("images/p13.gif");
        add.setIcon(icon4);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField textField = new JTextField(10);

        panel.add(new JLabel("Type a production/actor"));
        panel.add(textField);
        panel.add(add);

        add(panel);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                for (Movie m : imdb.movie) {
                    if (m.title.equals(s)) {
                        imdb.movie.remove(m);
                        JOptionPane.showMessageDialog(null, "Movie removed!");
                        return;
                    }
                }
                for (Series s2 : imdb.series) {
                    if (s2.title.equals(s)) {
                        imdb.series.remove(s2);
                        JOptionPane.showMessageDialog(null, "Series removed!");
                        return;
                    }
                }
                for (Actor a : imdb.actor) {
                    if (a.name.equals(s)) {
                        imdb.actor.remove(a);
                        JOptionPane.showMessageDialog(null, "Actor removed!");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null, "Invalid production!");

            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

class UpdateProductionInterface extends JFrame{
    IMDB imdb;
    String username;
    String type;
    public JButton add;

    public UpdateProductionInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setResizable(false);

        add = new JButton();
        ImageIcon icon4 = new ImageIcon("images/p11.gif");
        add.setIcon(icon4);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField textField = new JTextField(10);
        JTextField textField2 = new JTextField(10);

        panel.add(new JLabel("Type a production"));
        panel.add(textField);
        panel.add(new JLabel("Type a description"));
        panel.add(textField2);
        panel.add(add);

        add(panel);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                String s2 = textField2.getText();
                for (Movie m : imdb.movie) {
                    if (m.title.equals(s)) {
                        for (Contributor c : imdb.contributor) {
                            if (c.username.equals(username)) {
                                c.updateProduction(m, s2);
                                c.notifications.add("Production updated!");
                                break;
                            }
                        }
                        for (Admin a : imdb.admin) {
                            if (a.username.equals(username)) {
                                a.updateProduction(m, s2);
                                a.notifications.add("Production updated!");
                                break;
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Production updated!");
                        return;
                    }
                }
                for (Series ser : imdb.series) {
                    if (ser.title.equals(s)) {
                        for (Contributor c : imdb.contributor) {
                            if (c.username.equals(username)) {
                                c.updateProduction(ser, s2);
                                c.notifications.add("Production updated!");
                                break;
                            }
                        }
                        for (Admin a : imdb.admin) {
                            if (a.username.equals(username)) {
                                a.updateProduction(ser, s2);
                                a.notifications.add("Production updated!");
                                break;
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Production updated!");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null, "Invalid production!");
            }
        });
    }
}

class UpdateActorInterface extends JFrame{
    IMDB imdb;
    String username;
    String type;
    public JButton add;

    public UpdateActorInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setResizable(false);

        add = new JButton();
        ImageIcon icon4 = new ImageIcon("images/p11.gif");
        add.setIcon(icon4);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField textField = new JTextField(10);
        JTextField textField2 = new JTextField(10);

        panel.add(new JLabel("Type an actor"));
        panel.add(textField);
        panel.add(new JLabel("Type a bio"));
        panel.add(textField2);
        panel.add(add);

        add(panel);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                String s2 = textField2.getText();
                for (Actor act : imdb.actor) {
                    if (act.name.equals(s)) {
                        for (Contributor c : imdb.contributor) {
                            if (c.username.equals(username)) {
                                c.updateActor(act, s2);
                                c.notifications.add("Actor updated!");
                                break;
                            }
                        }
                        for (Admin a : imdb.admin) {
                            if (a.username.equals(username)) {
                                a.updateActor(act, s2);
                                a.notifications.add("Actor updated!");
                                break;
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Actor updated!");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null, "Invalid actor!");
            }
        });
    }
}

class ViewRequestsAdmInterface extends JFrame {
    IMDB imdb;
    String username;
    String type;
    public JButton back;
    public JButton add;

    public ViewRequestsAdmInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add = new JButton();
        ImageIcon icon = new ImageIcon("images/p11.gif");
        add.setIcon(icon);

        back = new JButton();
        ImageIcon icon3 = new ImageIcon("images/p9.gif");
        back.setIcon(icon3);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        int nr2 = 1;
        panel.add(new JLabel("Requests: "));
        for (Admin adm : imdb.admin) {
            if (adm.username.equals(username)) {
                Integer nr = 1;
                for (Object r : adm.r) {
                    JPanel p = new JPanel();
                    p.add(new JLabel(nr.toString() + ") "));
                    Request req = (Request) r;
                    p.add(new JLabel(req.requestType.toString()));
                    p.add(new JLabel(req.name));
                    p.add(new JLabel(req.description));
                    p.add(new JLabel(req.username_requester));
                    nr = nr + 1;
                    panel.add(p);
                }
                nr2 = nr - 1;
                break;
            }
        }

        Integer nr3 = nr2;
        if (nr2 == 0) {
            nr3 = 1;
        }
        for (Request req : RequestsHolder.requests) {
            JPanel p = new JPanel();
            p.add(new JLabel(nr3.toString() + ") "));
            Request req2 = (Request) req;
            p.add(new JLabel(req2.requestType.toString()));
            p.add(new JLabel(req2.name));
            p.add(new JLabel(req2.description));
            p.add(new JLabel(req2.username_requester));
            nr3 = nr3 + 1;
            panel.add(p);
        }

        panel.add(new JLabel("\n"));
        panel.add(new JLabel("Select a number request to accept:"));
        JTextField textField = new JTextField(10);
        panel.add(textField);
        panel.add(new JLabel("Deny request?"));
        JTextField textField2 = new JTextField(10);
        panel.add(textField2);
        panel.add(add);

        panel.add(back);
        add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);

        int finalNr = nr3 - 1;
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                String s2 = textField2.getText();
                if (Integer.parseInt(s) > finalNr || Integer.parseInt(s) < 1) {
                    JOptionPane.showMessageDialog(null, "Invalid number!");
                    return;
                }
                Integer nr4 = 0;
                for (Admin adm : imdb.admin) {
                    if (adm.username.equals(username)) {
                        Integer nr3 = 1;
                        for (Object o : adm.r) {
                            if (Integer.parseInt(s) == nr3) {
                                Request req = (Request) o;
                                adm.r.remove(o);
                                String usr = req.username_requester;
                                for (Regular r : imdb.regular) {
                                    if (r.username.equals(usr)) {
                                        if (s2.equals("yes")) {
                                            r.notifications.add("Request denied!");
                                            JOptionPane.showMessageDialog(null, "Request denied!");
                                            return;
                                        }
                                        ExprerienceStrategy xp = new addRequestExperience();
                                        r.setExprerienceStrategy(xp);
                                        r.updateExperience();
                                        r.notifications.add("Request resolved!");
                                        break;
                                    }
                                }
                                for (Contributor cont : imdb.contributor) {
                                    if (cont.username.equals(usr)) {
                                        if (s2.equals("yes")) {
                                            cont.notifications.add("Request denied!");
                                            JOptionPane.showMessageDialog(null, "Request denied!");
                                            return;
                                        }
                                        ExprerienceStrategy xp = new addRequestExperience();
                                        cont.setExprerienceStrategy(xp);
                                        cont.updateExperience();
                                        cont.notifications.add("Request resolved!");
                                        break;
                                    }
                                }
                                break;
                            }
                            nr3 = nr3 + 1;
                        }
                        nr4 = nr3 - 1;
                        if (nr3 == 1) {
                            nr4 = 1;
                        }
                    }
                }
                for (Request req : RequestsHolder.requests) {
                    if (nr4.equals(Integer.parseInt(s))) {
                        String usr = req.username_requester;
                        RequestsHolder.requests.remove(req);
                        for (Regular r : imdb.regular) {
                            if (r.username.equals(usr)) {
                                if (s2.equals("yes")) {
                                    r.notifications.add("Request denied!");
                                    JOptionPane.showMessageDialog(null, "Request denied!");
                                    return;
                                }
                                ExprerienceStrategy xp = new addRequestExperience();
                                r.setExprerienceStrategy(xp);
                                r.updateExperience();
                                r.notifications.add("Request resolved!");
                                break;
                            }
                        }
                        for (Contributor cont : imdb.contributor) {
                            if (cont.username.equals(usr)) {
                                if (s2.equals("yes")) {
                                    cont.notifications.add("Request denied!");
                                    JOptionPane.showMessageDialog(null, "Request denied!");
                                    return;
                                }
                                ExprerienceStrategy xp = new addRequestExperience();
                                cont.setExprerienceStrategy(xp);
                                cont.updateExperience();
                                cont.notifications.add("Request resolved!");
                                break;
                            }
                        }
                        break;
                    }
                    nr4 = nr4 + 1;
                }
                JOptionPane.showMessageDialog(null, "Request accepted!");
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type.equals("regular")) {
                    RegUserInterface regUserInterface = new RegUserInterface(imdb, username);
                    regUserInterface.setVisible(true);
                    dispose();
                } else if (type.equals("contributor")) {
                    ContrUserInterface contributorInterface = new ContrUserInterface(imdb, username);
                    contributorInterface.setVisible(true);
                    dispose();
                } else if (type.equals("admin")) {
                    AdminUserInterface adminInterface = new AdminUserInterface(imdb, username);
                    adminInterface.setVisible(true);
                    dispose();
                }
            }
        });
    }
}

class RemoveUserInterface extends JFrame{
    IMDB imdb;
    String username;
    String type;
    public JButton remove;

    public RemoveUserInterface(IMDB imdb, String username, String type) {
        this.imdb = imdb;
        this.username = username;
        this.type = type;

        setTitle("IMDB");
        setSize(1000, 600);
        setResizable(false);

        remove = new JButton();
        ImageIcon icon4 = new ImageIcon("images/p13.gif");
        remove.setIcon(icon4);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Type a username"));
        JTextField textField = new JTextField(10);
        panel.add(textField);
        panel.add(remove);

        add(panel);

        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textField.getText();
                for (Regular r : imdb.regular) {
                    if (r.username.equals(s)) {
                        imdb.regular.remove(r);
                        JOptionPane.showMessageDialog(null, "User removed!");
                        return;
                    }
                }
                for (Contributor c : imdb.contributor) {
                    if (c.username.equals(s)) {
                        imdb.contributor.remove(c);
                        JOptionPane.showMessageDialog(null, "User removed!");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null, "Invalid user!");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

class SearchMovieSerialGenre extends JFrame{
    public IMDB imdb;
    String type;
    String genre;
    Genre g;
    public SearchMovieSerialGenre(IMDB imdb, String type, String genre) {
        this.imdb = imdb;
        this.type = type;
        this.genre = genre;

        if (genre.equals("action")) {
            g = Genre.Action;
        } else if (genre.equals("comedy")) {
            g = Genre.Comedy;
        } else if (genre.equals("drama")) {
            g = Genre.Drama;
        } else if (genre.equals("horror")) {
            g = Genre.Horror;
        } else if (genre.equals("thriller")) {
            g = Genre.Thriller;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid genre!");
            return;
        }

        setTitle("IMDB");
        setSize(1000, 600);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (type.equals("movie")) {
            for (Movie m : imdb.movie) {
                for (Genre ge : m.Genres) {
                    if (ge == g) {
                        JLabel title = new JLabel("Title: " + m.getTitle());
                        JLabel year = new JLabel("Release Year: " + m.getYear_of_Release().toString());
                        JLabel rating = new JLabel("Rating: " + m.getTotalRating().toString());
                        JLabel genres = new JLabel("Genres: " + m.getGenres().toString());
                        JLabel duration = new JLabel("Duration: " + m.getDuration().toString());
                        JLabel actors = new JLabel("Actors: " + m.getActors().toString());
                        JLabel directors = new JLabel("Directors: " + m.getDirectors().toString());
                        JLabel description = new JLabel("Description: " + m.getDescription());
                        JPanel p = new JPanel();
                        p.add(title);
                        p.add(year);
                        p.add(rating);
                        p.add(genres);
                        p.add(duration);
                        p.add(actors);
                        p.add(directors);
                        if (m.Description != null) {
                            p.add(description);
                        }
                        p.setLayout(new FlowLayout(FlowLayout.LEFT));
                        panel.add(p);
                        panel.add(new JLabel("\n"));
                        add(panel);
                        break;
                    }
                }
            }
        }
        else if (type.equals("series")) {
            for (Series s : imdb.series) {
                for (Genre ge : s.Genres) {
                    if (ge.equals(g)) {
                        JLabel title = new JLabel("Title: " + s.getTitle());
                        JLabel year = new JLabel("Release Year: " + s.getYear().toString());
                        JLabel rating = new JLabel("Rating: " + s.getTotalRating().toString());
                        JLabel genres = new JLabel("Genres: " + s.getGenres().toString());
                        JLabel actors = new JLabel("Actors: " + s.getActors().toString());
                        JLabel directors = new JLabel("Directors: " + s.getDirectors().toString());
                        JLabel description = new JLabel("Description: " + s.getDescription());
                        JLabel seasons = new JLabel("Number of seasons: " + s.getNumberOfSeasons().toString());

                        JPanel p = new JPanel();
                        p.add(title);
                        p.add(year);
                        p.add(rating);
                        p.add(genres);
                        p.add(actors);
                        if (s.Description != null) {
                            p.add(directors);
                        }
                        p.add(description);
                        p.add(seasons);

                        for (Map.Entry<String, List<Episode>> entry : s.episodes.entrySet()) {
                            if (entry.getKey() != null) {
                                JLabel episodes = new JLabel(entry.getKey());
                                p.add(episodes);
                                for (Episode i : entry.getValue()) {
                                    JLabel title2 = new JLabel("Title: " + i.getTitle());
                                    JLabel duration = new JLabel("Duration: " + i.getDuration().toString());
                                    p.add(title2);
                                    p.add(duration);
                                }
                            }
                        }
                        p.setLayout(new FlowLayout(FlowLayout.LEFT));
                        panel.add(p);
                        panel.add(new JLabel("\n"));
                        add(panel);
                    }
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}