from flask import Flask, send_file
from flask import request
import os
import subprocess
import zipfile
import uuid
import shutil
from gevent import monkey
from gevent.pywsgi import WSGIServer

monkey.patch_all()

app = Flask(__name__)
app.config.update(DEBUG=True)


@app.route('/proto/gen', methods=['POST'])
def proto_gen():
    orig_plugin_dir = 'plugins'
    plugin_dir = 'cache/plugins_%s' % uuid.uuid1()
    shutil.copytree(orig_plugin_dir, plugin_dir)
    stype = request.args['type']
    out_type = request.args['out_type']
    filename = request.args['filename']

    save_dir = '%s/%s.proto' % (plugin_dir, filename)
    zip_save_dir = '%s/%s.zip' % (plugin_dir, filename)
    ret = None
    if stype == 'file':
        for f in request.files:
            request.files[f].save(save_dir)
            break
    elif stype == 'text':
        text = request.form['text']
        if text is not None:
            f = open(save_dir, 'w')
            f.write(text)
            f.close()
    elif stype == 'zip':
        for f in request.files:
            request.files[f].save(zip_save_dir)
            f = zipfile.ZipFile(zip_save_dir, 'r')
            for file in f.namelist():
                f.extract(file, "%s/" % plugin_dir)
            break
    else:
        ret = 'type error'

    run_plugin(plugin_dir, filename)

    if ret is not None:
        pass
    elif out_type == 'file':
        out_name = os.path.splitext(filename)[0] + '.cs'
        ret = send_file('%s/%s.cs' % (plugin_dir, filename), as_attachment=True, attachment_filename=out_name)
    elif out_type == 'text':
        ret = read_text('%s/%s.cs' % (plugin_dir, filename))
    else:
        ret = 'out type error'

    del_dir(plugin_dir)
    return ret


def del_dir(mdir):
    import time

    def del_async_wait():
        time.sleep(1)
        ct = 0
        while ct < 10:
            try:
                shutil.rmtree(mdir)
                print('del  %s' % mdir)
                break
            except:
                ct += 1
                time.sleep(1)

    import threading
    t = threading.Thread(target=del_async_wait, name='LoopThread')
    t.start()


def read_text(path):
    f = open(path)
    content = f.read()
    f.close()
    return content


def run_plugin(plugindir, name):
    plugin = os.path.abspath(os.curdir) + '/%s/protogen.exe' % plugindir
    args = '-i:%s.proto -o:%s.cs' % (name, name)
    p = subprocess.Popen(plugin + ' ' + args, cwd=os.path.abspath(os.curdir) + '/%s' % plugindir)
    p.wait()


if __name__ == '__main__':
    # app.run(host='0.0.0.0', port=8081)
    http_server = WSGIServer(('0.0.0.0', 8081), app)
    http_server.serve_forever()
