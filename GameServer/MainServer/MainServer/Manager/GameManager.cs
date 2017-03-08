using System.Collections;
using System.Timers;
namespace RedStone
{
    public class GameManager : Core.Singleton<GameManager>
    {
        public void Init()
        {
            DBManager.CreateInstance();
            ProtoTool.CreateInstance();
            ServerManager.CreateInstance();
            ProxyManager.CreateInstance();
            EventManager.CreateInstance();

            DBManager.instance.Init();
            ProtoTool.instance.Init();
            ServerManager.instance.Init();
            ProxyManager.instance.Init();

            TimerInit();
        }

        private Timer timer = new Timer(1d / 15); // 15 frames per second
        private void TimerInit()
        {
            timer.Start();
            timer.Elapsed += Timer_Elapsed;
        }

        private void Timer_Elapsed(object sender, ElapsedEventArgs e)
        {
            Update();
        }

        private void Update()
        {
            ServerManager.instance.Update();
            ProxyManager.instance.Update();
        }

        private void OnDestroy()
        {
            ProxyManager.instance.Destroy();
            EventManager.instance.ClearAll();
        }

        private void OnApplicationQuit()
        {
            ServerManager.instance.StopAll();
        }
    }
}