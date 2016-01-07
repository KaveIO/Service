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

  def test_get_authentication_token(self):
      token = self.obtain_token()

      self.assertTrue(len(token['access_token']) > 0)
      self.assertTrue(token['token_type'] == 'bearer')

  def test_use_token(self): 
      token = self.obtain_token()
      url = '%s/Service/test/layer/visit' % self.target

      req = urllib2.Request(url, "{}", {
          "Content-Type": "application/json",
          "Authorization": "Bearer %s" % token['access_token']})
      f = urllib2.urlopen(req, context=self.ctx)

      self.assertEquals(200, f.code)

      body = f.read()
      
      self.assertTrue(len(body) > 0)
      
      bodyJson = json.loads(body)
      self.assertTrue(bodyJson[0]['measurementTimestamp'] > 0)
      
  def test_use_basic(self):
      url = '%s/Service/test/layer/visit' % self.target

      req = urllib2.Request(url, "{}", {
          "Content-Type": "application/json",
          "Authorization": "Basic %s" % base64.encodestring('test:admin').replace('\n', '')})
      f = urllib2.urlopen(req, context=self.ctx)

      self.assertEquals(200, f.code)

      body = f.read()

      self.assertTrue(len(body) > 0)

      bodyJson = json.loads(body)
      self.assertTrue(bodyJson[0]['measurementTimestamp'] > 0)






if __name__ == '__main__':
    unittest.main()
