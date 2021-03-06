#!/usr/bin/env python
# -*- coding: UTF-8 -*-
#
# generated by wxGlade 0.6.8 on Mon Sep 16 15:48:51 2013
#

import wx
from subprocess import call
from subprocess import Popen
from subprocess import PIPE
import time
import os

# begin wxGlade: dependencies
import gettext
# end wxGlade

# begin wxGlade: extracode
# end wxGlade

PREDABSTR= "./PredAbstr/pred_abstr"
JAVATINYC= "./PredAbstr/JavaTinyC.jar"

import threading
import subprocess

EVT_RESULT_ID = wx.NewId()

class StatusDisplay(threading.Thread):
	def setData(self, frame):
		self.frame= frame
		self.interval= 1
		self.finished= False
	
	def run(self):
		print 'Thread started!'
		while True:
			try:
				if self.frame.p1 != None:
					if self.frame.p1.poll() is None:
						line= self.frame.p1.stderr.readline()
						# print 'Timer' + line.strip()
						val= line.strip()[:-1]
						# self.frame.gauge.SetValue(int(float(val)))
						wx.PostEvent(self.frame, StatusEvent(int(float(val))))
						# self.frame.Update()
				if self.finished:
					return
			except Exception as e:
				print e
				time.sleep(self.interval)

def EVT_RESULT(win, func):
	"""Define Result Event."""
	win.Connect(-1, -1, EVT_RESULT_ID, func)

class StatusEvent(wx.PyEvent):
	"""Simple event to carry arbitrary status data."""
	def __init__(self, data):
		"""Init Result Event."""
		wx.PyEvent.__init__(self)
		self.SetEventType(EVT_RESULT_ID)
		self.data = data
	


class Worker(threading.Thread):
	def setData(self, frame, prog, image, guess, preds):
		self.frame= frame
		self.prog= prog
		self.image= image
		self.guess= guess
		self.preds= preds
		self.killed= False
		
	def run(self):
		# res= call(["java", "-jar", JAVATINYC, self.prog])
		self.frame.p1= Popen(["java", "-jar", JAVATINYC, self.prog])
		
		self.frame.p1.communicate()
		self.frame.p1.poll()
		res= self.frame.p1.returncode
		
		if self.killed:
			return
		
		if res != 0 :
			wx.MessageBox('Executing java-tinyc failed', 'Error',  wx.OK)
			# dlg.ShowModal()
		else:
			commands= []
			commands.append(PREDABSTR)
			if self.image: #write image
				commands.append("-i")
				out= open(self.prog + ".dot","w")
			else: #output kripe structure
				out= open(self.prog + ".k")
			
			commands.append(self.prog)
			if not self.guess: # predicates entered via GUI -> add them
				for p in self.preds:
					commands.append(p)
			
			print commands
			
			self.frame.timer = StatusDisplay()
			self.frame.timer.setData(self.frame)
			self.frame.timer.start()
			self.frame.p1 = Popen(commands, stdout=out, stderr=PIPE)
			self.frame.p1.wait()
			self.frame.timer.finished= True
			
			if self.killed:
				return
			
			self.frame.p1.poll()
			res= self.frame.p1.returncode
			if res != 0 :
				wx.MessageBox('Predicate Abstraction failed', 'Error',  wx.OK)
			else:
				if self.image:
					pic= open(self.prog + ".png","w")
					dot= open(self.prog + ".dot","r")
					p2 = Popen(["dot", "-Tpng"], stdin=dot, stdout=pic)
					p2.communicate()
					self.frame.p1.poll()
					res= self.frame.p1.returncode
					if res == 0 :
						os.system(self.frame.viewer.GetValue() + " " + self.prog + ".png &")
					else:
						print 'Error creating picture'
			
			self.frame.timer.join()
			self.frame.timer= None
		
		print 'Done'
		if not self.killed:
			wx.PostEvent(self.frame, StatusEvent(None))
		# self.frame.enableButtons()

class MainFrame(wx.Frame):
  def __init__(self, *args, **kwds):
    # begin wxGlade: MainFrame.__init__
    kwds["style"] = wx.DEFAULT_FRAME_STYLE
    wx.Frame.__init__(self, *args, **kwds)
    self.label_1 = wx.StaticText(self, wx.ID_ANY, _("Program: "))
    self.text_ctrl_1 = wx.TextCtrl(self, wx.ID_ANY, "",size=wx.Size(340,27))
    self.button_2 = wx.Button(self, wx.ID_ANY, _("Choose"))
    self.checkbox_1 = wx.CheckBox(self, wx.ID_ANY, _("Guess"))
    self.list_ctrl_1 = wx.ListCtrl(self, wx.ID_ANY, style=wx.LC_REPORT | wx.SUNKEN_BORDER,size=wx.Size(340,400))
    self.button_plus = wx.Button(self, wx.ID_ANY, _("+"))
    self.button_minus = wx.Button(self, wx.ID_ANY, _("-"))
    self.checkpic = wx.CheckBox(self, wx.ID_ANY, _("Picture"))
    self.button_start = wx.Button(self, wx.ID_ANY, _("Run"))
    self.gauge= wx.Gauge(self, wx.ID_ANY, 100, size=wx.Size(150,20))
    self.viewer= wx.ComboBox(self, wx.ID_ANY,  choices=["xv", "gwenview", "eog"])
    self.viewer.SetStringSelection("xv")
    
    self.list_ctrl_1.InsertColumn(0,"Predicates")
    self.list_ctrl_1.SetColumnWidth(0, 340)
    
    self.normalcolour= wx.Colour(255,255,255,255)
    # self.normalcolor.Set
    self.disabledcolour= wx.Colour()
    self.disabledcolour.Set(180,180,180,255)
    self.enabled= True
    
    self.p1= None
    self.worker= None
    
    self.__set_properties()
    self.__do_layout()
    
    self.Bind(wx.EVT_BUTTON, self.onFileChoose, self.button_2)
    self.Bind(wx.EVT_CHECKBOX, self.onGuess, self.checkbox_1)
    self.Bind(wx.EVT_BUTTON, self.onAdd, self.button_plus)
    self.Bind(wx.EVT_BUTTON, self.onDelete, self.button_minus)
    self.Bind(wx.EVT_BUTTON, self.onRun, self.button_start)
    self.Bind(wx.EVT_CLOSE, self.onClose)
    
    EVT_RESULT(self,self.OnStatus)
    # end wxGlade
  
  def __set_properties(self):
    # begin wxGlade: MainFrame.__set_properties
    self.SetTitle(_("Praedicate Abstraction"))
    # end wxGlade
  
  def __do_layout(self):
    # begin wxGlade: MainFrame.__do_layout
    sizer_1 = wx.FlexGridSizer(4, 3, 0, 0)
    
    sizer_1.Add(self.label_1, 0, 0, 0)
    sizer_1.Add(self.text_ctrl_1, 0, 0, 0)
    sizer_1.Add(self.button_2, 0, 0, 0)
    sizer_1.Add(self.checkbox_1, 0, 0, 0)
    sizer_1.Add(self.list_ctrl_1, 1, wx.EXPAND, 0)
    
    btSizer= wx.GridSizer(2, 1, 0, 0)
    btSizer.Add(self.button_plus, 0, wx.TOP, 0)
    btSizer.Add(self.button_minus, 0, 0, 0)
    sizer_1.Add(btSizer, 0, 0, 0)
    
    sizer_1.Add(self.checkpic, 0, 0, 0)
    hh= wx.FlexGridSizer()
    hh.Add(self.viewer, 0, 0, 0)
    hh.Add(self.gauge, 0, wx.EXPAND, 0)
    # sizer_1.Add(self.button_start, 0, 0, 0)
    # sizer_1.Add(self.gauge, 0, wx.EXPAND, 0)
    sizer_1.Add(hh, 0, wx.EXPAND, 0)
    sizer_1.Add(self.button_start, 0, 0, 0)
    
    
    self.SetSizer(sizer_1)
    sizer_1.Fit(self)
    self.Layout()
    # end wxGlade
    
  def onFileChoose(self, event):  # wxGlade: MainFrame.<event_handler>
    dlg = wx.FileDialog(self, "Open a file", style=wx.FD_OPEN)
    wildcard = "Tiny C files (.tc|*.tc|" \
    "All files (*.*)|*.*"
    dlg.SetWildcard(wildcard)
    if dlg.ShowModal() == wx.ID_OK:
      # print dlg.GetFilename()
      self.text_ctrl_1.SetValue(dlg.GetPath())
      # event.Skip()
      
  def onGuess(self, event):  # wxGlade: MainFrame.<event_handler>
    if self.checkbox_1.IsChecked():
      # self.list_ctrl_1.SetEnabled(False)
      self.enabled= False
      for i in range (0,self.list_ctrl_1.GetItemCount()):
        self.list_ctrl_1.SetItemBackgroundColour(i,self.disabledcolour)
    else:
      # self.list_ctrl_1.SetEnabled(True)
      self.enabled= True
      for i in range (0,self.list_ctrl_1.GetItemCount()):
        self.list_ctrl_1.SetItemBackgroundColour(i,self.normalcolour)
		
    event.Skip()
    
  def onAdd(self, event):  # wxGlade: MainFrame.<event_handler>
    dialog = wx.TextEntryDialog(self, "","Add Predicate", "Predicate", style=wx.OK|wx.CANCEL)
    if dialog.ShowModal() == wx.ID_OK:
      res= dialog.GetValue()
      # print "You entered: %s" % res
      # self.list_ctrl_1.Append(res)
      self.list_ctrl_1.InsertStringItem(self.list_ctrl_1.GetItemCount(), res)
      # print self.list_ctrl_1.GetItemBackgroundColour(0)
      if self.enabled == False:
      	self.list_ctrl_1.SetItemBackgroundColour(self.list_ctrl_1.GetItemCount()-1,self.disabledcolour)
    
    event.Skip()
      	
  def onDelete(self, event):
    itm= self.list_ctrl_1.GetFirstSelected()
    if itm != -1:
      self.list_ctrl_1.DeleteItem(itm)
      event.Skip()
      
  def onRun(self, event):
  	self.disableButtons()
  	
  	preds= []
  	for i in range (0,self.list_ctrl_1.GetItemCount()):
			preds.append(str(self.list_ctrl_1.GetItem(i).GetText()))
		
  	self.worker= Worker()
  	self.worker.setData(self, self.text_ctrl_1.GetValue(),self.checkpic.IsChecked(),self.checkbox_1.IsChecked(), preds)
  	self.worker.start()
  	# worker.join()
  	
   	event.Skip()
  
  def disableButtons(self):
  	self.button_2.Disable()
  	self.button_plus.Disable()
  	self.button_minus.Disable()
  	self.button_start.Disable()
  
  def enableButtons(self):
  	self.button_2.Enable()
  	self.button_plus.Enable()
  	self.button_minus.Enable()
  	self.button_start.Enable()
  
  def OnStatus(self, event):
  	if event.data is None:
  		# Thread done or aborted
  		self.gauge.SetValue(100)
  		self.enableButtons()
  	else:
  		# Process results here
  		self.gauge.SetValue(event.data)
  
  def onClose(self, event):
  	if self.worker != None and self.worker.isAlive():
  		self.worker.killed= True
  		self.p1.kill()
  	
		if self.timer != None:
			self.timer.finished= True
			self.timer.join()
		
		if self.worker != None:
			self.worker.join()
		
  	self.Destroy()
  

# end of class MainFrame
class MyApp(wx.App):
  def OnInit(self):
    wx.InitAllImageHandlers()
    frame_1 = MainFrame(None, wx.ID_ANY, "")
    self.SetTopWindow(frame_1)
    frame_1.Show()
    return 1
    
# end of class MyApp

if __name__ == "__main__":
  gettext.install("app") # replace with the appropriate catalog name
  
  app = MyApp(0)
  app.MainLoop()
