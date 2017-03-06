using System;
using message;
using RedStone.Net;
namespace RedStone
{
    public class ProxyBase
    {
        public T GetProxy<T>() where T : ProxyBase
        {
            return ProxyManager.instance.GetProxy<T>();
        }

        public void SendEvent(string eventName, object obj = null)
        {
            EventManager.instance.Send(eventName, obj);
        }

        public void RegisterNet<T1, T2>(Action<T1, T2> handler)
        {
            string eventName = typeof(T2).ToString();
            EventManager.instance.Register(eventName, handler);
        }

        public void UnRegisterNet<T1, T2>(Action<T1, T2> handler)
        {
            string eventName = typeof(T2).ToString();
            EventManager.instance.UnRegister(eventName, handler);
        }

        public ProxyBase()
        {

        }

        public virtual void OnInit()
        {

        }

        public virtual void OnUpdate()
        {

        }

        public virtual void OnDestroy()
        {

        }
    }
}
