import urllib.request

def fetch(url):
    try:
        return urllib.request.urlopen(url, timeout=20).read().decode('utf-8')
    except Exception as e:
        return f'ERROR: {e}'

urls = {
    'main': 'https://raw.githubusercontent.com/andrestubbe/FastKeylogger/main/src/main/java/fastkeylogger/SessionStorage.java',
    'tag': 'https://raw.githubusercontent.com/andrestubbe/FastKeylogger/0.1.0/src/main/java/fastkeylogger/SessionStorage.java',
}
for name, url in urls.items():
    print('===', name, '===')
    data = fetch(url)
    if data.startswith('ERROR:'):
        print(data)
    else:
        lines = data.splitlines()
        for i, line in enumerate(lines[:20], 1):
            print(f'{i:02d}: {line}')
        print('... total', len(lines))
