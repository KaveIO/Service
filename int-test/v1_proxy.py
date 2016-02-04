import unittest
import urllib2
import urllib
import ssl
import base64
import json

class TestOAuth(unittest.TestCase):

  target = 'https://localhost:8443'
  
  ctx = ssl.create_default_context()
  ctx.check_hostname = False
  ctx.verify_mode = ssl.CERT_NONE
  
  def obtain_token(self):
      data = urllib.urlencode({"grant_type":"client_credentials"})
      url = '%s/oauth-server/j_oauth_token_grant' % self.target
      basicAuth = "Basic %s" % base64.encodestring('test:admin').replace('\n', '')
      req = urllib2.Request(url, data, {
          "Authorization": basicAuth})
      f = urllib2.urlopen(req, context=self.ctx)
      return json.loads(f.read())

  def test_get_plain(self): 
      token = self.obtain_token()
      url = '%s/Service/v1/proxy/test/test' % self.target

      req = urllib2.Request(url, headers={
          "Authorization": "Bearer %s" % token['access_token']})
      f = urllib2.urlopen(req, context=self.ctx)

      self.assertEquals(200, f.code)

      body = f.read()
     
      self.assertTrue(len(body) > 0)
      
      bodyJson = json.loads(body)


if __name__ == '__main__':
    unittest.main()
