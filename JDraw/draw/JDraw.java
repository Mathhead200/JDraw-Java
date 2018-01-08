package draw;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import com.mathhead200.image.*;
import com.mathhead200.math.*;


public class JDraw
{
	public static class JDrawFields {
		public String red = "0", green = "0", blue = "0", alpha = "100",
			xMin = "-100", xMax = "100", yMin = "-100", yMax = "100";
		public File currentFile = null;
		public int imgWidth = 400, imgHeight = 300;

		public String toString() {
			return "red=" + red + ",green=" + green + ",blue=" + blue + ",alpha=" + alpha
				+ ",xMin=" + xMin + ",xMax=" + xMax + ",yMin=" + yMin + ",yMax=" + yMax
				+ ",currentFile=" + currentFile
				+ ",imgWidth=" + imgWidth + ",imgHeight=" + imgHeight;
		}
	}

	public static void main(String[] args)
	{
		final JDrawFields fields = new JDrawFields();
		final JFrame mainFrame = new JFrame("JDraw");
		final DisplayLabel pallet;
		{
			DisplayLabel tempDL = null;
			try {
				tempDL = new DisplayLabel( EquationImage.load(args[0]) );
			} catch(ArrayIndexOutOfBoundsException e) {
				tempDL = new DisplayLabel(fields.imgWidth, fields.imgHeight);
			} catch(IOException e) {
				tempDL = new DisplayLabel(fields.imgWidth, fields.imgHeight);
			} catch(NullImageException e) {
				tempDL = new DisplayLabel(fields.imgWidth, fields.imgHeight);
			}
			pallet = tempDL;
		}

		mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		mainFrame.setExtendedState( JFrame.MAXIMIZED_BOTH );
		mainFrame.setContentPane( new JPanel( new GridBagLayout() ) );

		mainFrame.setMenuBar( new MenuBar() );
		mainFrame.getMenuBar().add( new Menu("File") );
		mainFrame.getMenuBar().add( new Menu("Edit") );

		mainFrame.getMenuBar().getMenu(0).add( new MenuItem("New") );
		mainFrame.getMenuBar().getMenu(0).add( new MenuItem("Open") );
		mainFrame.getMenuBar().getMenu(0).add( new MenuItem("Save") );

		mainFrame.getMenuBar().getMenu(1).add( new MenuItem("Draw") );
		mainFrame.getMenuBar().getMenu(1).add( new MenuItem("Tint") );
		mainFrame.getMenuBar().getMenu(1).add( new MenuItem("Invert") );
		mainFrame.getMenuBar().getMenu(1).add( new MenuItem("Blur") );
		mainFrame.getMenuBar().getMenu(1).add( new MenuItem("Flip") );
		mainFrame.getMenuBar().getMenu(1).add( new MenuItem("Shift") );
		mainFrame.getMenuBar().getMenu(1).add( new MenuItem("Rotate") );
		mainFrame.getMenuBar().getMenu(1).add( new MenuItem("Resize") );

		//New
		mainFrame.getMenuBar().getMenu(0).getItem(0).addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame window = new JFrame("JDraw - File - New");
				window.setContentPane( new JPanel( new GridLayout(3, 2, 10, 10) ) );
				final JTextField widthField = new JTextField(""+pallet.getImage().getWidth(), 4),
					heightField =new JTextField(""+pallet.getImage().getHeight(), 4);
				final JButton confirmButton = new JButton("Create"),
					cancelButton = new JButton("Cancel");

				window.getContentPane().add( new JLabel("Width: ") );
				window.getContentPane().add( widthField );
				window.getContentPane().add( new JLabel("Height: ") );
				window.getContentPane().add( heightField );
				window.getContentPane().add( confirmButton );
				window.getContentPane().add( cancelButton );

				confirmButton.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fields.imgWidth = Integer.parseInt( widthField.getText() );
						fields.imgHeight = Integer.parseInt( heightField.getText() );
						pallet.setImage( new EquationImage(fields.imgWidth, fields.imgHeight) );
						mainFrame.validate();
						window.dispose();
					}
				});
				cancelButton.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						window.dispose();
					}
				});

				window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				window.pack();
				window.validate();
				window.setVisible(true);
			}
		});
		//Open
		mainFrame.getMenuBar().getMenu(0).getItem(1).addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame browser = new JFrame("JDraw - File - Open");
				final JFileChooser fc = new JFileChooser( fields.currentFile );
				fc.setFileFilter( new FileFilter() {
					public boolean accept(File f) { return f.isDirectory() || f.getName().endsWith(".png"); }
					public String getDescription() { return "PNG"; }
				});
				fc.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						File loadFile = fc.getSelectedFile();
						fields.currentFile = loadFile;
						try {
							if( e.getActionCommand().equals("ApproveSelection") ) {
								EquationImage img = EquationImage.load( loadFile );
								pallet.setImage(img);
								mainFrame.setTitle( "JDraw - " + loadFile.getName() );
								mainFrame.validate();
								browser.dispose();
							} else if( e.getActionCommand().equals("CancelSelection") ) {
								browser.dispose();
							}
						} catch(IOException f) {
							f.printStackTrace();
							JOptionPane.showMessageDialog( fc, f, loadFile.getAbsolutePath(),
								JOptionPane.ERROR_MESSAGE );
						}
					}
				});
				browser.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				browser.getContentPane().add(fc);
				browser.pack();
				browser.validate();
				browser.setVisible(true);
			}
		});
		//Save
		mainFrame.getMenuBar().getMenu(0).getItem(2).addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame browser = new JFrame("JDraw - File - Save");
				final JFileChooser fc = new JFileChooser( fields.currentFile );
				final FileFilter ffPng = new FileFilter() {
					public boolean accept(File f) { return f.isDirectory() || f.getName().endsWith(".png"); }
					public String getDescription() { return "PNG"; }
				};
				fc.setFileFilter(ffPng);
				fc.setApproveButtonText("Save");
				fc.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							if( e.getActionCommand().equals("ApproveSelection") ) {
								File saveFile = fc.getSelectedFile();
								fields.currentFile = saveFile;
								if( fc.getFileFilter() == ffPng && !saveFile.getName().endsWith(".png") )
									saveFile = new File( saveFile.getAbsolutePath() + ".png" );
								if( pallet.getImage().save(saveFile) ) {
									JOptionPane.showMessageDialog( fc, "File was saved!",
										saveFile.getAbsolutePath(), JOptionPane.INFORMATION_MESSAGE );
									browser.dispose();
								}
								else if( JOptionPane.showConfirmDialog(fc,
									"This file already exists. Overwrite?", saveFile.getAbsolutePath(),
									JOptionPane.YES_NO_OPTION ) == JOptionPane.OK_OPTION
								) {
									pallet.getImage().overwrite(saveFile);
									browser.dispose();
								}
							} else if( e.getActionCommand().equals("CancelSelection") ) {
								browser.dispose();
							}
						} catch(IOException f) {
							f.printStackTrace();
						}
					}
				});
				browser.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				browser.getContentPane().add(fc);
				browser.pack();
				browser.validate();
				browser.setVisible(true);
			}
		});
		//Draw
		mainFrame.getMenuBar().getMenu(1).getItem(0).addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame window = new JFrame("JDraw - Edit - Draw");
				window.setContentPane( new JPanel( new GridBagLayout() ) );
				final JTextField
					redField = new JTextField( fields.red, 100 ),
					greenField = new JTextField( fields.green, 100 ),
					blueField = new JTextField( fields.blue, 100 ),
					alphaField = new JTextField( fields.alpha, 100 ),
					xMinField = new JTextField( fields.xMin, 10 ),
					xMaxField = new JTextField( fields.xMax, 10 ),
					yMinField = new JTextField( fields.yMin, 10 ),
					yMaxField = new JTextField( fields.yMax, 10 );
				final JButton
					drawButton = new JButton("Draw"),
					cancelButton = new JButton("Cancel");
				{
					@SuppressWarnings("serial")
					class LGBC extends GridBagConstraints {
						public LGBC(int y, boolean onTop, boolean onBottom) {
							this.anchor = GridBagConstraints.LINE_END;
							gridx = 0;
							gridy = y;
							insets = new Insets( onTop ? 10 : 0, 10, onBottom ? 10 : 15, 5 );
							if(!onBottom) weightx = 1;
						}
						public LGBC(int y) { this(y, true, true); }
					}
					@SuppressWarnings("serial")
					class RGBC extends GridBagConstraints {
						public RGBC(int y, boolean onTop, boolean onBottom) {
							this.anchor = GridBagConstraints.LINE_START;
							gridx = 1;
							gridy = y;
							insets = new Insets( onTop ? 10 : 0, 0, onBottom ? 10 : 15, 10 );
							if(!onBottom) weightx = 9;
						}
						public RGBC(int y) { this(y, false, false); }
					}
					window.getContentPane().add( new JLabel("red(x,y) = "), new LGBC(0, true, false) );
					window.getContentPane().add( redField, new RGBC(0, true, false) );
					window.getContentPane().add( new JLabel("green(x,y) = "), new LGBC(1) );
					window.getContentPane().add( greenField, new RGBC(1) );
					window.getContentPane().add( new JLabel("blue(x,y) = "), new LGBC(2) );
					window.getContentPane().add( blueField, new RGBC(2) );
					window.getContentPane().add( new JLabel("alpha(x,y) = "), new LGBC(3) );
					window.getContentPane().add( alphaField, new RGBC(3) );
					window.getContentPane().add( new JLabel("xMin = "), new LGBC(4) );
					window.getContentPane().add( xMinField, new RGBC(4) );
					window.getContentPane().add( new JLabel("xMax = "), new LGBC(5) );
					window.getContentPane().add( xMaxField, new RGBC(5) );
					window.getContentPane().add( new JLabel("yMin = "), new LGBC(6) );
					window.getContentPane().add( yMinField, new RGBC(6) );
					window.getContentPane().add( new JLabel("yMax = "), new LGBC(7) );
					window.getContentPane().add( yMaxField, new RGBC(7) );
					window.getContentPane().add( drawButton, new LGBC(8, false, true) );
					window.getContentPane().add( cancelButton, new RGBC(8, false, true) );
				}
				drawButton.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fields.red = redField.getText();
						fields.green = greenField.getText();
						fields.blue = blueField.getText();
						fields.alpha = alphaField.getText();
						fields.xMin = xMinField.getText();
						fields.xMax = xMaxField.getText();
						fields.yMin = yMinField.getText();
						fields.yMax = yMaxField.getText();
						final String frameTitle = mainFrame.getTitle();
						window.setVisible(false);
						pallet.getImage().draw(
							Double.parseDouble(fields.xMin), Double.parseDouble(fields.xMax),
							Double.parseDouble(fields.yMin), Double.parseDouble(fields.yMax),
							new ColorEquations() {
								public void initCalc(double x, double y) {
									mainFrame.setTitle( String.format("%1$s: Drawing... (%2$ .3f,%3$ .3f)", frameTitle, x, y) );
								}
								public double red(double x, double y) {
									StringEquation eq = new StringEquation(fields.red);
									eq.substitute("x", x);
									eq.substitute("y", y);
									return eq.solve();
								}
								public double green(double x, double y) {
									StringEquation eq = new StringEquation(fields.green);
									eq.substitute("x", x);
									eq.substitute("y", y);
									return eq.solve();
								}
								public double blue(double x, double y) {
									StringEquation eq = new StringEquation(fields.blue);
									eq.substitute("x", x);
									eq.substitute("y", y);
									return eq.solve();
								}
								public double alpha(double x, double y) {
									StringEquation eq = new StringEquation(fields.alpha);
									eq.substitute("x", x);
									eq.substitute("y", y);
									return eq.solve();
								}
							}
						);
						pallet.redraw();
						mainFrame.setTitle(frameTitle);
						mainFrame.validate();
						window.dispose();
					}
				});
				cancelButton.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						window.dispose();
					}
				});

				window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				window.pack();
				window.validate();
				window.setVisible(true);
			}
		});
		//Tint
		mainFrame.getMenuBar().getMenu(1).getItem(1).addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame window = new JFrame("JDraw - Edit - Tint");
				window.setContentPane( new JPanel( new GridLayout(5, 2, 5, 10) ) );
				final JTextField redField = new JTextField("0", 5),
					greenField = new JTextField("0", 5),
					blueField = new JTextField("0", 5),
					alphaField = new JTextField("0", 5);
				final JButton confirm = new JButton("Tint"),
					cancel = new JButton("Cancel");
				window.getContentPane().add( new JLabel("Red: ") );
				window.getContentPane().add( redField );
				window.getContentPane().add( new JLabel("Green: ") );
				window.getContentPane().add( greenField );
				window.getContentPane().add( new JLabel("Blue: ") );
				window.getContentPane().add( blueField );
				window.getContentPane().add( new JLabel("Alpha: ") );
				window.getContentPane().add( alphaField );
				window.getContentPane().add( confirm );
				window.getContentPane().add( cancel );

				confirm.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						pallet.setImage( pallet.getImage().tint(
							Integer.parseInt( redField.getText() ),
							Integer.parseInt( greenField.getText() ),
							Integer.parseInt( blueField.getText() ),
							Integer.parseInt( alphaField.getText() )
						));
						mainFrame.validate();
						window.dispose();
					}
				});
				cancel.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						window.dispose();
					}
				});

				window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				window.pack();
				window.validate();
				window.setVisible(true);
			}
		});
		//Invert
		mainFrame.getMenuBar().getMenu(1).getItem(2).addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame window = new JFrame("JDraw - Edit - Invert");
				window.setContentPane( new JPanel( new GridLayout(2, 2, 10, 10) ) );
				final JCheckBox colorBox = new JCheckBox("Invert Color", true),
					alphaBox = new JCheckBox("Invert Alpha", false);
				final JButton confirm = new JButton("Invert"),
					cancel = new JButton("Cancel");
				window.getContentPane().add( colorBox );
				window.getContentPane().add( alphaBox );
				window.getContentPane().add( confirm );
				window.getContentPane().add( cancel );
				confirm.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if( colorBox.isSelected() )
							pallet.setImage( pallet.getImage().invert() );
						if( alphaBox.isSelected() )
							pallet.setImage( pallet.getImage().invertAlpha() );
						mainFrame.validate();
						window.dispose();
					}
				});
				cancel.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						window.dispose();
					}
				});
				window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				window.pack();
				window.validate();
				window.setVisible(true);
			}
		});
		//Blur
		mainFrame.getMenuBar().getMenu(1).getItem(3).addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame window = new JFrame("JDraw - Edit - Blur");
				window.setContentPane( new JPanel( new GridLayout(3, 2, 5, 10) ) );
				final JTextField xField = new JTextField("0", 4),
					yField = new JTextField("0", 4);
				final JButton confirm = new JButton("Blur"),
					cancel = new JButton("cancel");
				window.getContentPane().add( new JLabel("x: ") );
				window.getContentPane().add( xField );
				window.getContentPane().add( new JLabel("y: ") );
				window.getContentPane().add( yField );
				window.getContentPane().add( confirm );
				window.getContentPane().add( cancel );

				confirm.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final String frameTitle = mainFrame.getTitle();
						mainFrame.setTitle( frameTitle + ": Bluring..." );
						window.setVisible(false);
						pallet.setImage( pallet.getImage().blur(
							Integer.parseInt( xField.getText() ), Integer.parseInt( yField.getText() )
						));
						mainFrame.setTitle(frameTitle);
						window.dispose();
					}
				});
				cancel.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						window.dispose();
					}
				});
				window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				window.pack();
				window.validate();
				window.setVisible(true);
			}
		});
		//Flip
		mainFrame.getMenuBar().getMenu(1).getItem(4).addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame window = new JFrame("JDraw - Edit - Flip");
				window.setContentPane( new JPanel( new GridBagLayout() ) );
				final JCheckBox horBox = new JCheckBox("Horizontal Flip"),
					vertBox = new JCheckBox("Vertical Flip"),
					transBox = new JCheckBox("Transpose");
				final JButton confirm = new JButton("Flip"),
					cancel = new JButton("Cancel");

				@SuppressWarnings("serial")
				class GBC extends GridBagConstraints {
					public GBC() {
						gridwidth = 2;
						anchor = GridBagConstraints.CENTER;
						insets = new Insets(5, 5, 5, 5);
					}
				}
				GridBagConstraints gbc = new GBC();
				window.getContentPane().add(horBox, gbc);
				gbc = new GBC();
				gbc.gridy = 1;
				window.getContentPane().add(vertBox, gbc);
				gbc = new GBC();
				gbc.gridy = 2;
				window.getContentPane().add(transBox, gbc);
				gbc = new GridBagConstraints();
				gbc.gridy = 3;
				window.getContentPane().add(confirm, gbc);
				gbc = new GridBagConstraints();
				gbc.gridy = 3;
				gbc.gridx = 1;
				window.getContentPane().add(cancel, gbc);

				{
					ActionListener hvListener = new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							transBox.setVisible( !horBox.isSelected() && !vertBox.isSelected() );
						}
					};
					horBox.addActionListener(hvListener);
					vertBox.addActionListener(hvListener);
				}
				transBox.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						horBox.setVisible( !transBox.isSelected() );
						vertBox.setVisible( !transBox.isSelected() );
					}
				});
				confirm.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if( horBox.isVisible() && horBox.isSelected() )
							pallet.setImage( pallet.getImage().flipHorizontal() );
						if( vertBox.isVisible() && vertBox.isSelected() )
							pallet.setImage( pallet.getImage().flipVertical() );
						if( transBox.isVisible() && transBox.isSelected() )
							pallet.setImage( pallet.getImage().transpose() );
						mainFrame.validate();
						window.dispose();
					}
				});
				cancel.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						window.dispose();
					}
				});

				window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				window.pack();
				window.validate();
				window.setVisible(true);
			}
		});
		//Shift
		mainFrame.getMenuBar().getMenu(1).getItem(5).addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame window = new JFrame("JDraw - Edit - Shift");
				window.setContentPane( new JPanel( new GridLayout(3, 2, 5, 10) ) );
				final JTextField rField = new JTextField("0", 5),
					dField = new JTextField("0", 5);
				final JButton confirm = new JButton("Shift"),
					cancel = new JButton("Cancel");
				window.getContentPane().add( new JLabel("Right: ") );
				window.getContentPane().add( rField );
				window.getContentPane().add( new JLabel("Down: ") );
				window.getContentPane().add( dField );
				window.getContentPane().add( confirm );
				window.getContentPane().add( cancel );

				confirm.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						pallet.setImage( pallet.getImage().shift(
							Integer.parseInt( rField.getText() ),
							Integer.parseInt( dField.getText() )
						));
						mainFrame.validate();
						window.dispose();
					}
				});
				cancel.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						window.dispose();
					}
				});

				window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				window.pack();
				window.validate();
				window.setVisible(true);
			}
		});
		//Rotate
		mainFrame.getMenuBar().getMenu(1).getItem(6).addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame window = new JFrame("JDraw - Edit - Rotate");
				window.setContentPane( new JPanel( new GridBagLayout() ) );
				final JRadioButton rb90 = new JRadioButton("90 Degrees", true),
					rb180 = new JRadioButton("180 Degrees"),
					rb270 = new JRadioButton("270 Degrees"),
					rbOther = new JRadioButton("Other");
				final ButtonGroup angles = new ButtonGroup();
				angles.add(rb90);
				angles.add(rb180);
				angles.add(rb270);
				angles.add(rbOther);
				final JTextField otherField = new JTextField("", 4);
				final JButton confirm = new JButton("Rotate"),
					cancel = new JButton("Cancel");

				{
					@SuppressWarnings("serial")
					class GBC extends GridBagConstraints {
						public GBC(int x, int y, int w) {
							gridx = x;
							gridy = y;
							gridwidth = w;
							anchor = GridBagConstraints.CENTER;
						}
					}

					window.getContentPane().add( rb90, new GBC(0, 0, 2) );
					window.getContentPane().add( rb180, new GBC(0, 1, 2) );
					window.getContentPane().add( rb270, new GBC(0, 2, 2) );
					window.getContentPane().add( rbOther, new GBC(0, 3, 1) );
					window.getContentPane().add( otherField, new GBC(1, 3, 1) );
					window.getContentPane().add( confirm, new GBC(0, 4, 1) );
					window.getContentPane().add( cancel, new GBC(1, 4, 1) );
				}
				otherField.addKeyListener( new KeyListener() {
					public void keyPressed(KeyEvent e) {}
					public void keyReleased(KeyEvent e) {}
					public void keyTyped(KeyEvent e) {
						if( !rbOther.isSelected() )
							rbOther.doClick();
					}
				});
				confirm.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							if( rb90.isSelected() )
								pallet.setImage( pallet.getImage().rotate() );
							else if( rb180.isSelected() )
								pallet.setImage( pallet.getImage().rotate().rotate() );
							else if( rb270.isSelected() )
								pallet.setImage( pallet.getImage().rotateC() );
							else if( rbOther.isSelected() )
								pallet.setImage(
									pallet.getImage().rotate( Double.parseDouble(otherField.getText()) ) );
							window.dispose();
						} catch(NumberFormatException f) {
							JOptionPane.showMessageDialog(window, "Not a valid angle.");
						}
					}
				});
				cancel.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						window.dispose();
					}
				});
				window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				window.pack();
				window.validate();
				window.setVisible(true);
			}
		});
		//Resize
		mainFrame.getMenuBar().getMenu(1).getItem(7).addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("serial")
				class RBX extends JRadioButton {
					public boolean wasSelected;
					public RBX(String text, boolean selected) {
						super(text, selected);
						wasSelected = selected;
					}
				}
				final JFrame window = new JFrame("JDraw - Edit - Resize");
				window.setContentPane( new JPanel( new GridLayout(4, 2, 10, 10) ) );
				final ButtonGroup bg = new ButtonGroup();
				final RBX rbPixel = new RBX("by Pixels", true),
					rbRatio = new RBX("by Ratio", false);
				bg.add(rbPixel);
				bg.add(rbRatio);
				final JTextField horField = new JTextField( ""+pallet.getImage().getWidth(), 5 ),
					vertField = new JTextField( ""+pallet.getImage().getHeight(), 5 );
				final JButton confirm = new JButton("Resize"),
					cancel = new JButton("Cancel");

				window.getContentPane().add(rbPixel);
				window.getContentPane().add(rbRatio);
				window.getContentPane().add( new JLabel("Horizontal: ") );
				window.getContentPane().add(horField);
				window.getContentPane().add( new JLabel("Vertical: ") );
				window.getContentPane().add(vertField);
				window.getContentPane().add(confirm);
				window.getContentPane().add(cancel);

				rbPixel.addChangeListener( new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						if( !rbPixel.isSelected() || rbPixel.wasSelected )
							return;
						rbPixel.wasSelected = true;
						rbRatio.wasSelected = false;
						Double width = StringEquation.parseEquation( horField.getText() ),
							height = StringEquation.parseEquation( vertField.getText() );
						if( !width.isNaN() ) {
							String num = width * pallet.getImage().getWidth() + "";
							num = num.substring( 0, num.indexOf('.') );
							horField.setText(num);
						}
						if( !height.isNaN() ) {
							String num = height * pallet.getImage().getHeight() + "";
							num = num.substring( 0, num.indexOf('.') );
							vertField.setText(num);
						}
					}
				});
				rbRatio.addChangeListener( new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						if( !rbRatio.isSelected() || rbRatio.wasSelected )
							return;
						rbRatio.wasSelected = true;
						rbPixel.wasSelected = false;
						try {
							horField.setText(
								Integer.parseInt( horField.getText() ) / (double)pallet.getImage().getWidth() + "" );
						} catch(NumberFormatException f) {}
						try {
							vertField.setText(
								Integer.parseInt( vertField.getText() ) / (double)pallet.getImage().getHeight() + "" );
						} catch(NumberFormatException f) {}
					}
				});
				confirm.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							if( rbPixel.isSelected() )
								pallet.setImage( pallet.getImage().stretch(
									Integer.parseInt( horField.getText() ),
									Integer.parseInt( vertField.getText() )
								));
							else if( rbRatio.isSelected() ) {
								Double width = StringEquation.parseEquation( horField.getText() ),
									height = StringEquation.parseEquation( vertField.getText() );
								if( height.isNaN() || width.isNaN() )
									throw new Exception();
								pallet.setImage( pallet.getImage().stretchByRatio(width, height) );
							}
						} catch(NumberFormatException f) {
							JOptionPane.showMessageDialog(window, "Invalid Integer.");
						} catch(Exception f) {
							JOptionPane.showMessageDialog(window, "Invalid Number or Equation");
						}
						mainFrame.validate();
						window.dispose();
					}
				});
				cancel.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						window.dispose();
					}
				});

				window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				window.pack();
				window.validate();
				window.setVisible(true);
			}
		});

		/*
		 * GridBagConstraints(
		 *	int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty,
		 *	int anchor, int fill, Insets insets, int ipadx, int ipady
		 * )
		 */
		mainFrame.add( pallet , new GridBagConstraints(
			0, 0, 0, 0, 0, 0,
			GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0
		));

		mainFrame.validate();
		mainFrame.setVisible(true);
	}
}
