using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using RedStone;
using RedStone.Net;

namespace MainServer
{
    public partial class MainForm : Form
    {
        public MainForm()
        {
            InitializeComponent();
            GameManager.CreateInstance().Init();
        }

        private void btnStartGameServer_Click(object sender, EventArgs e)
        {
            ServerManager.instance.Start(NetType.Main);
        }

        private void btnStartBattleServer_Click(object sender, EventArgs e)
        {
            ServerManager.instance.Start(NetType.Battle);
        }
    }
}
