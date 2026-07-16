import urllib.request
urls = [
    'https://jitpack.io/com/github/andrestubbe/FastKeylogger/0.1.0/build.log',
    'https://jitpack.io/com/github/andrestubbe/FastKeylogger/0.1.0/build.log?force=true',
]
for url in urls:
    print('URL:', url)
    try:
        with urllib.request.urlopen(url, timeout=30) as r:
            text = r.read().decode('utf-8', errors='replace')
        print('LENGTH', len(text))
        print(text[:1500])
    except Exception as e:
        print('ERROR', e)
    print('---')
