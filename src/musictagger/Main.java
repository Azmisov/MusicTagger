/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musictagger;

import musictagger.tagtable.TagTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import musictagger.musicfiles.MusicFile;
import musictagger.musicfiles.MusicTag;
import musictagger.tagtable.TagTableListen;
import org.jaudiotagger.tag.FieldKey;

/**
 *
 * @author isaac
 */
public class Main extends javax.swing.JFrame {
	/**
	 * Creates new form Main
	 */
	public Main() {
		//Initialize GUI
		tagger = new Tagger();
		initComponents();

		/* Listen for tag edits */
		model = (TagTableModel) tblTags.getModel();
		model.addTableModelListener(new TagTableListen(){
			@Override
			public void tableChanged(MusicTag tag, String oldVal, String newVal) {
				int err = tagger.editTags(
					lstFiles.getSelectedIndices(),
					tag, oldVal, newVal
				);
				if (err > 0)
					showErrMessage("Could not edit "+err+" tag(s).\nTry opening the file and editing it again.");
				hasEdits = true;
				getTagsFromSel();
			}
		});
		
		genericModel = new DefaultComboBoxModel(FieldKey.values());
		comboName.setModel(genericModel);
		
		//Load settings file
		loadSettings();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        fileChooseDialog = new javax.swing.JDialog();
        fileChooser = new javax.swing.JFileChooser();
        addTagDialog = new javax.swing.JDialog();
        comboType = new javax.swing.JComboBox();
        addDialogBtnCancel = new javax.swing.JButton();
        addDialogBtnOkay = new javax.swing.JButton();
        txtValue = new javax.swing.JTextField();
        lblTag = new javax.swing.JLabel();
        lblValue = new javax.swing.JLabel();
        txtCustom = new javax.swing.JTextField();
        comboName = new javax.swing.JComboBox();
        chkMultiple = new javax.swing.JCheckBox();
        fileRenameDialog = new javax.swing.JDialog();
        nameDialogBtnOkay = new javax.swing.JButton();
        nameDialogBtnCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtExpression = new javax.swing.JTextField();
        splitPane = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTags = new musictagger.tagtable.TagTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstFiles = new javax.swing.JList();
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        mBtnOpen = new javax.swing.JMenuItem();
        mBtnClear = new javax.swing.JMenuItem();
        mBtnSave = new javax.swing.JMenuItem();
        menuView = new javax.swing.JMenu();
        mChckEmpty = new javax.swing.JCheckBoxMenuItem();
        mChckGeneric = new javax.swing.JCheckBoxMenuItem();
        menuTags = new javax.swing.JMenu();
        mBtnAdd = new javax.swing.JMenuItem();
        mBtnDelete = new javax.swing.JMenuItem();
        mBtnRename = new javax.swing.JMenuItem();

        fileChooseDialog.setTitle("Load music files and folders");
        fileChooseDialog.setAlwaysOnTop(true);
        fileChooseDialog.setLocationByPlatform(true);
        fileChooseDialog.setMinimumSize(new java.awt.Dimension(585, 378));

        fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });
        fileChooser.setFileFilter(tagger.filter);

        javax.swing.GroupLayout fileChooseDialogLayout = new javax.swing.GroupLayout(fileChooseDialog.getContentPane());
        fileChooseDialog.getContentPane().setLayout(fileChooseDialogLayout);
        fileChooseDialogLayout.setHorizontalGroup(
            fileChooseDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fileChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
        );
        fileChooseDialogLayout.setVerticalGroup(
            fileChooseDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fileChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
        );

        addTagDialog.setTitle("Add new tag");
        addTagDialog.setMinimumSize(new java.awt.Dimension(400, 155));
        addTagDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        java.awt.GridBagLayout addTagDialogLayout = new java.awt.GridBagLayout();
        addTagDialogLayout.columnWidths = new int[] {0, 6, 0, 6, 0, 6, 0};
        addTagDialogLayout.rowHeights = new int[] {0, 6, 0, 6, 0};
        addTagDialog.getContentPane().setLayout(addTagDialogLayout);

        comboType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Generic", "All", "Custom" }));
        comboType.setToolTipText("");
        comboType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        addTagDialog.getContentPane().add(comboType, gridBagConstraints);

        addDialogBtnCancel.setText("Cancel");
        addDialogBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDialogBtnCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        addTagDialog.getContentPane().add(addDialogBtnCancel, gridBagConstraints);

        addDialogBtnOkay.setText("Okay");
        addDialogBtnOkay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDialogBtnOkayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        addTagDialog.getContentPane().add(addDialogBtnOkay, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        addTagDialog.getContentPane().add(txtValue, gridBagConstraints);

        lblTag.setText("Tag:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 4, 0);
        addTagDialog.getContentPane().add(lblTag, gridBagConstraints);

        lblValue.setText("Value:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 8);
        addTagDialog.getContentPane().add(lblValue, gridBagConstraints);

        txtCustom.setText("CUSTOM_TAG");
        txtCustom.setVisible(false);
        txtCustom.setMinimumSize(new java.awt.Dimension(206, 25));
        txtCustom.setPreferredSize(new java.awt.Dimension(206, 25));
        txtCustom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 0, 11);
        addTagDialog.getContentPane().add(txtCustom, gridBagConstraints);

        comboName.setMinimumSize(new java.awt.Dimension(206, 24));
        comboName.setPreferredSize(new java.awt.Dimension(206, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        addTagDialog.getContentPane().add(comboName, gridBagConstraints);

        chkMultiple.setText("Allow multiple");
        chkMultiple.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMultipleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 0, 0);
        addTagDialog.getContentPane().add(chkMultiple, gridBagConstraints);

        addTagDialog.setMinimumSize(new java.awt.Dimension(400, 155));
        addTagDialog.setPreferredSize(new java.awt.Dimension(400, 155));

        fileRenameDialog.setTitle("File rename utility");
        fileRenameDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        nameDialogBtnOkay.setText("Okay");

        nameDialogBtnCancel.setText("Cancel");

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("<html> Text in between brackets (e.g. [ARTIST]) will be replaced with the respective tag value. File extensions are added automatically.");

        txtExpression.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        txtExpression.setText("[TRACK]_[TITLE]");
        txtExpression.setMinimumSize(new java.awt.Dimension(4, 24));

        javax.swing.GroupLayout fileRenameDialogLayout = new javax.swing.GroupLayout(fileRenameDialog.getContentPane());
        fileRenameDialog.getContentPane().setLayout(fileRenameDialogLayout);
        fileRenameDialogLayout.setHorizontalGroup(
            fileRenameDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileRenameDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fileRenameDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtExpression, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fileRenameDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(nameDialogBtnOkay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameDialogBtnCancel)))
                .addContainerGap())
        );
        fileRenameDialogLayout.setVerticalGroup(
            fileRenameDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileRenameDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtExpression, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(fileRenameDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameDialogBtnCancel)
                    .addComponent(nameDialogBtnOkay))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Music Tagger");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        splitPane.setResizeWeight(0.5);

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(tblTags);

        splitPane.setRightComponent(jScrollPane2);

        lstFiles.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstFilesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstFiles);

        splitPane.setLeftComponent(jScrollPane1);

        menuFile.setText("File");

        mBtnOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mBtnOpen.setText("Open...");
        mBtnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mBtnOpenActionPerformed(evt);
            }
        });
        menuFile.add(mBtnOpen);

        mBtnClear.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        mBtnClear.setText("Clear");
        mBtnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mBtnClearActionPerformed(evt);
            }
        });
        menuFile.add(mBtnClear);

        mBtnSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mBtnSave.setText("Save");
        mBtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mBtnSaveActionPerformed(evt);
            }
        });
        menuFile.add(mBtnSave);

        menuBar.add(menuFile);

        menuView.setText("View");

        mChckEmpty.setText("Show empty tags");
        mChckEmpty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mChckEmptyActionPerformed(evt);
            }
        });
        menuView.add(mChckEmpty);

        mChckGeneric.setSelected(true);
        mChckGeneric.setText("Display generic tag names");
        mChckGeneric.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mChckGenericActionPerformed(evt);
            }
        });
        menuView.add(mChckGeneric);

        menuBar.add(menuView);

        menuTags.setText("Tags");

        mBtnAdd.setText("Add...");
        mBtnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mBtnAddActionPerformed(evt);
            }
        });
        menuTags.add(mBtnAdd);

        mBtnDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        mBtnDelete.setText("Delete");
        mBtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mBtnDeleteActionPerformed(evt);
            }
        });
        menuTags.add(mBtnDelete);

        mBtnRename.setText("File rename utility...");
        menuTags.add(mBtnRename);

        menuBar.add(menuTags);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fileChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserActionPerformed
        //Get a list of files and send them to the Tagger class
		String action = evt.getActionCommand();
		if ("CancelSelection".equals(action)){
			fileChooseDialog.setVisible(false);
			return;
		}
		File[] fs = fileChooser.getSelectedFiles();
		//Save selection settings
		int[] oldSel = lstFiles.getSelectedIndices();
		//Load the file list
		setFileList(fs, oldSel);
    }//GEN-LAST:event_fileChooserActionPerformed

    private void lstFilesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstFilesValueChanged
		if (!defer && !evt.getValueIsAdjusting())
			getTagsFromSel();			
    }//GEN-LAST:event_lstFilesValueChanged

    private void mBtnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mBtnClearActionPerformed
        if (hasEdits){
			int n = this.showOptMessage("You have unsaved changes.\nDo you still want to clear these files?");
			if (n == 1) return;
		}
		//Clear the selection
		tagger.load(null, true);
		lstFiles.setListData(new Object[0]);
		getTagsFromSel();
    }//GEN-LAST:event_mBtnClearActionPerformed

    private void mBtnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mBtnOpenActionPerformed
        //Show the file chooser
		fileChooseDialog.setVisible(true);
    }//GEN-LAST:event_mBtnOpenActionPerformed

    private void mBtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mBtnSaveActionPerformed
        // TODO add your handling code here:
		if (hasEdits){
			int errors = tagger.commit();
			if (errors == 0)
				hasEdits = false;
			else showErrMessage("Could not save "+errors+" file(s).\nYou could try again, if you'd like...");
		}
    }//GEN-LAST:event_mBtnSaveActionPerformed
	
	//TAG ACTIONS
    private void mBtnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mBtnAddActionPerformed
        addTagDialog.setVisible(true);
    }//GEN-LAST:event_mBtnAddActionPerformed

    private void mBtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mBtnDeleteActionPerformed
        int[] files = lstFiles.getSelectedIndices();
		//Compile a list of tags to delete
		List<Map.Entry<MusicTag, String>> sel = model.getSelection();
		//Attempt deletion
		if (!sel.isEmpty()){
			int errs = tagger.deleteTags(files, sel);
			if (errs != files.length*sel.size())
				hasEdits = true;
			if (errs != 0)
				showErrMessage("Could not delete "+errs+" tag(s).");
			//Refresh view
			getTagsFromSel();
		}
    }//GEN-LAST:event_mBtnDeleteActionPerformed

	//ADD TAG ACTIONS
    private void txtCustomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomActionPerformed

    private void comboTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTypeActionPerformed
		//Show or hide the custom tag name input
		boolean useCustom = comboType.getModel().getSelectedItem().equals("Custom");
		comboName.setVisible(!useCustom);
		txtCustom.setVisible(useCustom);
    }//GEN-LAST:event_comboTypeActionPerformed

    private void addDialogBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDialogBtnCancelActionPerformed
        addTagDialog.setVisible(false);
    }//GEN-LAST:event_addDialogBtnCancelActionPerformed

    private void mChckEmptyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mChckEmptyActionPerformed
        optViewEmpty = mChckEmpty.getState();
		getTagsFromSel();
    }//GEN-LAST:event_mChckEmptyActionPerformed

    private void mChckGenericActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mChckGenericActionPerformed
        optViewGeneric = mChckGeneric.getState();
		getTagsFromSel();
    }//GEN-LAST:event_mChckGenericActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (hasEdits){
			int n = showOptMessage("You have unsaved changes.\nAre you sure you want to close?");
			if (n == 1) return;
		}
		saveSettings();
		this.dispose();
		System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void addDialogBtnOkayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDialogBtnOkayActionPerformed
		String tagname;
		/* Custom tag creation
		 * Send the custom tag as it is; individual MusicTag classes will
		 * validate it as they see fit.
		 */
		if ("Custom".equals(comboType.getSelectedItem()))
			tagname = txtCustom.getText();
		/* The selected item here is a FieldKey (or some related class)
		 * If we parse it to a string, the individual MusicTag classes will know
		 * what to do with it
		 */
		else tagname = comboName.getSelectedItem().toString();
		//Submit edits
		int[] sel = lstFiles.getSelectedIndices();
		int errs = tagger.addTags(sel, tagname, txtValue.getText());
		if (errs > 0)
			showErrMessage("Could not add tags to "+errs+" file(s).");
		if (errs != sel.length){
			hasEdits = true;
			//Refresh
			getTagsFromSel();
		}
		//Close dialog
		addTagDialog.setVisible(false);
    }//GEN-LAST:event_addDialogBtnOkayActionPerformed

    private void chkMultipleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMultipleActionPerformed
        allowMultiple = chkMultiple.isSelected();
    }//GEN-LAST:event_chkMultipleActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 * 
		 * Others:
		 * Metal, Nimbus, CDE/Motif, GTK+
		 */
		try {
			//String system_feel = UIManager.getSystemLookAndFeelClassName();
			//javax.swing.UIManager.setLookAndFeel(system_feel);
			//*
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
			//*
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		//WebLookAndFeel.install();

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Main x = new Main();
				x.setVisible(true);
				//Split pane needs to be visible before setting divider location
				x.setDivider();
			}
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDialogBtnCancel;
    private javax.swing.JButton addDialogBtnOkay;
    private javax.swing.JDialog addTagDialog;
    private javax.swing.JCheckBox chkMultiple;
    private javax.swing.JComboBox comboName;
    private javax.swing.JComboBox comboType;
    private javax.swing.JDialog fileChooseDialog;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JDialog fileRenameDialog;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTag;
    private javax.swing.JLabel lblValue;
    private javax.swing.JList lstFiles;
    private javax.swing.JMenuItem mBtnAdd;
    private javax.swing.JMenuItem mBtnClear;
    private javax.swing.JMenuItem mBtnDelete;
    private javax.swing.JMenuItem mBtnOpen;
    private javax.swing.JMenuItem mBtnRename;
    private javax.swing.JMenuItem mBtnSave;
    private javax.swing.JCheckBoxMenuItem mChckEmpty;
    private javax.swing.JCheckBoxMenuItem mChckGeneric;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuTags;
    private javax.swing.JMenu menuView;
    private javax.swing.JButton nameDialogBtnCancel;
    private javax.swing.JButton nameDialogBtnOkay;
    private javax.swing.JSplitPane splitPane;
    private musictagger.tagtable.TagTable tblTags;
    private javax.swing.JTextField txtCustom;
    private javax.swing.JTextField txtExpression;
    private javax.swing.JTextField txtValue;
    // End of variables declaration//GEN-END:variables
	
	private Tagger tagger;
	private TagTableModel model;
	private boolean hasEdits = false,
			optViewEmpty = false,
			optViewGeneric = true,
			allowMultiple = false,
			defer = true;
	private DefaultComboBoxModel genericModel, allModel;
	private UserSettings settings;
	
	private void setTags(Map<MusicTag, List<String>> rows){
		model.removeAllRows();
		model.loadData(rows);
	}
	private void getTagsFromSel(){
		//Get tag info from selection
		int[] i = lstFiles.getSelectedIndices();
		//Update
		if (i.length > 0){
			setTags(tagger.getMatchingTags(i, optViewEmpty, optViewGeneric));
		}
		//Or just clear the tag table
		else model.removeAllRows();
	}
	private void setFileList(File[] files, final int[] old_sel){
		if (files.length > 0){
			fileChooser.setSelectedFile(null);
			fileChooseDialog.setVisible(false);
			//Load the files
			int fail = tagger.load(files, false);
			lstFiles.setListData(tagger.listFiles());
			//Calling setSelectedIndices fires the selection listener event a bajillion times
			//This is idiotic, so we'll ignore all the events
			defer = true;
			if (old_sel.length == 0)
				lstFiles.setSelectedIndex(0);
			else lstFiles.setSelectedIndices(old_sel);
			defer = false;
			//Now that selection is done, we can simulate the selection event ourselves
			getTagsFromSel();
			
			//Show any errors...
			if (fail > 0)
				showErrMessage("Could not load "+fail+" file(s).");
		}
	}
	
	private int showOptMessage(String message){
		return JOptionPane.showOptionDialog(
			this, message, "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null
		);
	}
	private void showErrMessage(String message){
		JOptionPane.showMessageDialog(
			this, message, "Error", JOptionPane.ERROR_MESSAGE
		);
	}
	
	//Save & Load settings
	private void loadSettings(){
		try {
            FileInputStream fis = new FileInputStream("settings.dat");
			try (ObjectInputStream ois = new ObjectInputStream(fis)) {
				settings = (UserSettings) ois.readObject();
				//Load all the settings
				this.setLocation(settings.winMain.loc);
				this.setSize(settings.winMain.dims);
				this.setExtendedState(settings.winMain.max);
				
				addTagDialog.setLocation(settings.winAdd.loc);
				addTagDialog.setSize(settings.winAdd.dims);
				
				fileChooseDialog.setLocation(settings.winFile.loc);
				fileChooseDialog.setSize(settings.winFile.dims);
				
				fileRenameDialog.setLocation(settings.winRename.loc);
				fileRenameDialog.setSize(settings.winRename.dims);
				
				optViewEmpty = settings.showEmpty;
				optViewGeneric = settings.showGeneric;
				allowMultiple = settings.allowMultiple;
				mChckEmpty.setState(optViewEmpty);
				mChckGeneric.setState(optViewGeneric);
				chkMultiple.setSelected(allowMultiple);
				
				txtCustom.setText(settings.customTag);
				txtValue.setText(settings.tagValue);
				txtExpression.setText(settings.renameUtility);
				
				fileChooser.setCurrentDirectory(settings.dir);
				defer = false;
				setFileList(settings.files, settings.sel_files);
				//tblTags.getColumnModel().getColumn(0).setWidth(settings.tblcol_width);
			}
        } catch (IOException | ClassNotFoundException e) {
			settings = new UserSettings();
			addTagDialog.setLocationRelativeTo(null);
			fileChooseDialog.setLocationRelativeTo(null);
			setLocationRelativeTo(null);
        }
	}
	private void saveSettings(){
		settings.winMain.loc = this.getLocation();
		settings.winMain.dims = this.getSize();
		settings.winMain.max = this.getExtendedState();
		
		settings.winAdd.loc = addTagDialog.getLocation();
		settings.winAdd.dims = addTagDialog.getSize();
		
		settings.winFile.loc = fileChooseDialog.getLocation();
		settings.winFile.dims = fileChooseDialog.getSize();
		
		settings.winRename.loc = fileRenameDialog.getLocation();
		settings.winRename.dims = fileRenameDialog.getSize();
		
		settings.showEmpty = optViewEmpty;
		settings.showGeneric = optViewGeneric;
		settings.allowMultiple = allowMultiple;
		
		settings.customTag = txtCustom.getText();
		settings.tagValue = txtValue.getText();
		settings.renameUtility = txtExpression.getText();
		
		settings.dir = fileChooser.getCurrentDirectory();
		
		settings.sel_files = lstFiles.getSelectedIndices();

		MusicFile[] mfs = tagger.listFiles();
		File[] fs = new File[mfs.length];
		for (int i=0; i<mfs.length; i++)
			fs[i] = mfs[i].raw_file;
		settings.files = fs;
		
		settings.split_loc = splitPane.getDividerLocation();
		//settings.tblcol_width = tblTags.getColumnModel().getColumn(0).getWidth();
		
		//Save the settings file
		try {
            FileOutputStream fout = new FileOutputStream("settings.dat");
			try (ObjectOutputStream oos = new ObjectOutputStream(fout)) {
				oos.writeObject(settings);
			}
        } catch (Exception e) {
            System.out.println("Could not save settings...");
        }
	}
	
	private void setDivider(){
		splitPane.setDividerLocation(settings.split_loc);
	}
}
