using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace RedStone
{
	public class UIManager : Core.Singleton<UIManager>
	{
		private Dictionary<string, string> m_prefabs = new Dictionary<string, string>();

		private Dictionary<string, GameObject> m_gameObjects = new Dictionary<string, GameObject>();

		private Stack m_stack = new Stack();

		public Transform uiRoot;

		public void Init()
		{
			uiRoot = GameObject.Find("UI Root").transform;
			RegisteAll();
			PreLoad();
		}

		public void PreLoad()
		{
			foreach (var kv in m_prefabs)
			{
				Object obj = Resources.Load(MyPath.RES_UI + kv.Value);
				GameObject go = Object.Instantiate(obj) as GameObject;
				ViewBase _base = go.GetComponent<ViewBase>();
				m_gameObjects.Add(_base.GetType().ToString(), go);
				go.transform.SetParent(uiRoot,false);
				go.SetActive(false);
			}
		}

		public void RegisteAll()
		{
			AddUI("Login");
			AddUI("Gomuku");
		}

		public void AddUI(string name, string prefabPath = null)
		{
			if (prefabPath == null)
				prefabPath = name;
			m_prefabs.Add(name, prefabPath);
		}

		public void Show<T>()
		{
			string name = typeof(T).ToString();
			UIContent content = new UIContent(name);
			m_stack.Push(content);
			ShowGameObject(name);
		}

		public void CloseAll()
		{
			while (m_stack.Count > 0)
			{
				UIContent content = m_stack.Pop() as UIContent;
				m_gameObjects[content.name].SetActive(false);
			}
		}

		public void Back()
		{
			UIContent content = m_stack.Pop() as UIContent;
			m_gameObjects[content.name].SetActive(false);


			UIContent peek = m_stack.Peek() as UIContent;
			m_gameObjects[peek.name].SetActive(true);
		}

		private void ShowGameObject(string name)
		{
			UIContent peek = m_stack.Peek() as UIContent;
			m_gameObjects[peek.name].SetActive(true);

			// hide others
			foreach (UIContent obj in m_stack)
			{
				if (obj != peek)
					m_gameObjects[obj.name].SetActive(false);
			}
		}
	}

	public class UIContent
	{
		public string name;

		public UIContent(string name)
		{
			this.name = name;
		}
	}
}
