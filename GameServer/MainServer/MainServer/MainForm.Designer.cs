namespace MainServer
{
    partial class MainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.btnStartBattleServer = new System.Windows.Forms.Button();
            this.btnStartGameServer = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // btnStartBattleServer
            // 
            this.btnStartBattleServer.Location = new System.Drawing.Point(250, 152);
            this.btnStartBattleServer.Name = "btnStartBattleServer";
            this.btnStartBattleServer.Size = new System.Drawing.Size(130, 32);
            this.btnStartBattleServer.TabIndex = 0;
            this.btnStartBattleServer.Text = "BattleServer";
            this.btnStartBattleServer.UseVisualStyleBackColor = true;
            this.btnStartBattleServer.Click += new System.EventHandler(this.btnStartBattleServer_Click);
            // 
            // btnStartGameServer
            // 
            this.btnStartGameServer.Location = new System.Drawing.Point(68, 152);
            this.btnStartGameServer.Name = "btnStartGameServer";
            this.btnStartGameServer.Size = new System.Drawing.Size(114, 32);
            this.btnStartGameServer.TabIndex = 1;
            this.btnStartGameServer.Text = "GameServer";
            this.btnStartGameServer.UseVisualStyleBackColor = true;
            this.btnStartGameServer.Click += new System.EventHandler(this.btnStartGameServer_Click);
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(729, 440);
            this.Controls.Add(this.btnStartGameServer);
            this.Controls.Add(this.btnStartBattleServer);
            this.Name = "MainForm";
            this.Text = "Main";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnStartBattleServer;
        private System.Windows.Forms.Button btnStartGameServer;
    }
}

